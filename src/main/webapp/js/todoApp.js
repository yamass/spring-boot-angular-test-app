angular.module('todoApp', ['ngRoute', 'todoServices'])

    .controller('TodoController', ['$scope', 'Todo', function ($scope, Todo) {

        var socket = new SockJS('/messaging');

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/todos', function (frame) {
                $scope.$apply(function () {
                    var todoFromServer = JSON.parse(frame.body);
                    $scope.todos[todoFromServer.id] = todoFromServer;
                });
            });
        });

        $scope.todos = {};
        Todo.query().$promise.then(function (todos) {
            initTodos(todos);
        });

        function initTodos(todosFromServer) {
            $scope.todos = {};
            for (var i = 0; i < todosFromServer.length; i++) {
                var todo = todosFromServer[i];
                $scope.todos[todo.id] = todo;
            }
        }

        $scope.addTodo = function () {
            var newTodo = {text: $scope.todoText, done: false};

            Todo.save(newTodo, function (persistedTodo, responseHeaders) {
                $scope.todos[persistedTodo.id] = persistedTodo;
                $scope.todoText = '';
            }, function (httpResponse) {
                var validationErrors = httpResponse.data;
                $scope.serverValidationErrors = validationErrors;
                $scope.addTodoForm.$setValidity('server', true);
                angular.forEach(validationErrors.fieldErrors, function (fieldError) {
                    $scope.addTodoForm[fieldError.fieldName].$setValidity('server', false);
                });
            });
        };

        $scope.getServerValidationErrorsForField = function (fieldName) {
            var fieldErrors = [];
            angular.forEach($scope.serverValidationErrors.fieldErrors, function (fieldError) {
                if (fieldError.fieldName === fieldName) {
                    fieldErrors.push(fieldError);
                }
            });
            return fieldErrors;
        };

        $scope.numberOfTodos = function () {
            return Object.keys($scope.todos).length;
        };

        $scope.numberOfRemainingTodos = function () {
            var count = 0;
            angular.forEach($scope.todos, function (todo, id) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

        $scope.archive = function () {

        };

        $scope.updateTodo = function (todo) {
            Todo.update(todo, function (persistedTodo) {
                $scope.todos[persistedTodo.id] = persistedTodo;
            });
        };

        $scope.deleteTodo = function (todo) {
            Todo.delete({id: todo.id}, function () {
                delete $scope.todos[todo.id];
            });
        };

        $scope.deleteDoneTodos = function () {
            Todo.deleteDone(function(todosFromServer) {
                initTodos(todosFromServer);
            });
        };


    }])

    .controller('EditTodoController', ['$scope', 'Todo', 'todosUrl', '$routeParams', '$location', function ($scope, Todo, todosUrl, $routeParams, $location) {
        $scope.todo = Todo.get({id: $routeParams.todoId});

        $scope.saveTodo = function () {
            $scope.todo.$update();
            $location.path("#/todos");
        }
    }])

    .controller('MyPortletController', ['$scope', function ($scope) {
        $scope.message = 'hello Portlet...' + new Date().getMilliseconds();
    }])

    .filter('orderObjectBy', function () {
        return function (items, field, reverse) {
            var filtered = [];
            angular.forEach(items, function (item) {
                filtered.push(item);
            });
            filtered.sort(function (a, b) {
                return (a[field] > b[field] ? 1 : -1);
            });
            if (reverse) filtered.reverse();
            return filtered;
        };
    })


    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/todos', {
                    templateUrl: '../partials/todoList.html',
                    controller: 'TodoController'
                }).
                when('/todos/:todoId', {
                    templateUrl: '../partials/editTodo.html',
                    controller: 'EditTodoController'
                }).
                otherwise({
                    redirectTo: '/todos'
                });
        }])

    .directive('serverError', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, ctrl) {
                element.on('change', function () {
                    scope.$apply(function () {
                        ctrl.$setValidity('server', true)
                    })
                });
            }
        };
    })
;
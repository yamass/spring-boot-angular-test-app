angular.module('todoApp', ['ngRoute'])

    .value('todosUrl', 'http://localhost:8080/data/todos')

    .controller('TodoController', ['$scope', '$http', 'todosUrl', function ($scope, $http, todosUrl) {

        function initTodos(todosFromServer) {
            $scope.todos = {};
            for (var i = 0; i < todosFromServer.length; i++) {
                var todo = todosFromServer[i];
                $scope.todos[todo.id] = todo;
            }
        }

        $http.get(todosUrl).success(function (todosFromServer) {
            initTodos(todosFromServer);
        });

        $scope.addTodo = function () {
            var newTodo = {text: $scope.todoText, done: false};

            $http.post(todosUrl, newTodo).success(function (persistedTodo) {
                $scope.todos[persistedTodo.id] = persistedTodo;
                $scope.todoText = '';
            }).error(function () {
                console.log("ERROR while writing todo!");
            });
        };

        $scope.remaining = function () {
            var count = 0;
            angular.forEach($scope.todos, function (todo, id) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

        $scope.archive = function () {

        };

        $scope.updateTodo = function (todo) {
            $http.put(todosUrl, todo).success(function (persistedTodo) {
                console.log("successfully updated todo " + todo.id);
                $scope.todos[persistedTodo.id] = persistedTodo;
            });
        };

        $scope.deleteTodo = function (todo) {
            $http.delete(todosUrl + "/" + todo.id).success(function () {
                console.log("successfully delete todo " + todo.id);
                delete $scope.todos[todo.id];
            }).error(function () {
                console.log("ERROR while deleting todo " + todo.id);
            });
        };

        $scope.deleteDoneTodos = function () {
            $http.post(todosUrl + "/deleteDone").success(function (todosFromServer) {
                initTodos(todosFromServer);
            }).error(function () {
                console.log("ERROR while deleting done todos!");
            });
        };


    }])

    .controller('EditTodoController', ['$scope', '$http', 'todosUrl', '$routeParams', '$location', function ($scope, $http, todosUrl, $routeParams, $location) {
        $http.get(todosUrl + "/" + $routeParams.todoId).success(function (todoFromServer) {
            $scope.todo=todoFromServer;
        });

        $scope.saveTodo = function () {
            $http.put(todosUrl, $scope.todo).success(function (updatedTodo) {
                console.log("successfully updated todo " + $scope.todo);
                $location.path("#/todos");
            });
        }
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
        function($routeProvider) {
            $routeProvider.
                when('/todos', {
                    templateUrl: 'partials/todoList.html',
                    controller: 'TodoController'
                }).
                when('/todos/:todoId', {
                    templateUrl: 'partials/editTodo.html',
                    controller: 'EditTodoController'
                }).
                otherwise({
                    redirectTo: '/todos'
                });
        }])
;
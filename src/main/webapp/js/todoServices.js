angular.module('todoServices', ['ngResource'])

    .factory('Todo', ['$resource', function ($resource) {
        return $resource('/data/todo/:id', null, {
            'update': {method: 'PUT'},
            'deleteDone': {url: '/data/todo/deleteDone', method: 'POST', isArray: true}
        });
    }]);

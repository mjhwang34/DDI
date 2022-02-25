(function (window) {

	'use strict';
    angular
        .module('app', ['ngMaterial', 'ngMessages', 'ngResource', 'ngSanitize', 'pascalprecht.translate']);

	angular.module('app')
		.factory('API', ['$resource',
			function ($resource) {

				var api = {};

				api.user = $resource('/api/user/:cmd', { cmd: '@cmd' }, {
					'login': {
						method: 'post',
						params: { cmd: 'login' },
						headers: {"content-type": "application/x-www-form-urlencoded; charset=UTF-8"}
					},
					'is_login': {
						method: 'get',
						params: { cmd: 'is_login' }
					}
				});

                api.ddi = $resource('/api/ddi/:cmd', { cmd: '@cmd' }, {
					'drugs': {
						method: 'get',
						params: { cmd: 'drugs' }
					},
					'pk': {
						method: 'get',
						params: { cmd: 'pk' }
					},
					'protein': {
						method: 'get',
						params: { cmd: 'protein' }
					},
					'gene': {
						method: 'get',
						params: { cmd: 'gene' }
					},
					'substitution': {
						method: 'get',
						params: { cmd: 'substitution' }
					},
					//'network': {
						//method: 'get',
						//params: { cmd: 'network' }
					//}
				});

                api.dfi = $resource('/api/dfi/:cmd', { cmd: '@cmd' }, {
					'foods': {
						method: 'get',
						params: { cmd: 'foods' }
					},
					'drugs': {
						method: 'get',
						params: { cmd: 'drugs' }
					},
					'search': {
						method: 'get',
						params: { cmd: 'search' }
					}
				});
				return api;
			}
		]);
}(window));
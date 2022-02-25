(function (window) {
	'use strict';

    angular
        .module('app')
        .config(function ($mdDateLocaleProvider) {
            $mdDateLocaleProvider.formatDate = function (date) {
                if (date) return moment(date).format('YYYY-MM-DD');
                return "";
            }
        })
        .config(function ($mdThemingProvider) {
            $mdThemingProvider.theme('docs-dark', 'default')
                .primaryPalette('yellow')
                .dark();

        }).config(function ($mdAriaProvider) {
            $mdAriaProvider.disableWarnings();
        })
        .config(['$translateProvider', function ($translateProvider) {
            $translateProvider.translations('ko', message.ko);
            $translateProvider.translations('en', message.en);
			$translateProvider.translations('es', message.es);
            $translateProvider.preferredLanguage(locale);
        }])
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push('httpRequestInterceptor');
            $httpProvider.interceptors.push('httpResponseInterceptor');
        }])
        .run(['$rootScope', function ($rootScope) {
            console.log('config run')
        }]);
}(window));  
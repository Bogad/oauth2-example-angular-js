angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'home',
		controllerAs : 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.defaults.headers.common['Accept'] = 'application/json';

}).controller('navigation',

function($rootScope, $http, $location, $route) {

	var self = this;
	$rootScope.selectedRole = null;
	$rootScope.roles = [];
	$rootScope.showRoles =false;
	self.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	$http.get('user').then(function(response) {
		if (response.data.name) {
			$rootScope.authenticated = true;
			$http.get('resource/userHasRole').then(function(response1) {
				if(response1.data.check == "KO"){
					alert(response1.data.roles);
					if (response1.data.roles === null ||response1.data.length == 0) {
						alert("Vous n'avez aucune affectation enregistrée!!");
					}else{
						alert("Choix du rôle");
						$rootScope.roles = response1.data.roles;
						$rootScope.showRoles =true;
					}
				}else{
					alert("role OK");
				}
			}, function() {
				$rootScope.authenticated = false;
			});
			
		} else {
			$rootScope.authenticated = false;
		}
	}, function() {
		$rootScope.authenticated = false;
	});

	self.selectRole = function(role) {
		alert("Selection du rôle "+role);
		$rootScope.selectedRole = role
		$http.post('resource/selectRole', role,{
		      transformRequest: angular.identity,
		      transformResponse: angular.identity,
		      headers: {
		        'Content-Type': undefined
		      }}).then(function(response) {
			alert(response.data);
			$rootScope.showRoles =false;
		})
	}
	
	self.credentials = {};

	self.logout = function() {
		console.log("post logout to uaa using ajax...");

	   $.ajax({
	        url: "http://localhost:9999/uaa/logout",
	        method: "POST",
	        xhrFields: {
	            withCredentials: true
        },
	        success: function(data) {
				console.log("post logout to ui-gateway...");

				$http.post('logout', {}).finally(function() {
					console.log("redirect to root...");
							
					$rootScope.authenticated = false;
					$rootScope.showRoles = false;
					showRoles
					$location.path("/");
				});
	        }
    	})		
	}

}).controller('home', function($http,$location,$window) {
	var self = this;
	$http.get('resource/').then(function(response) {
		self.greeting = response.data;
	})
	self.fetchCall = function() {
		alert("ok");
		$http.get('resource/actionconsulter', {
	      transformRequest: angular.identity,
	      transformResponse: angular.identity,
	      headers: {
	        'Content-Type': undefined
	      }}).then(function(response) {
	    	  alert(response.data);
			self.call = response.data;
		}, function (msg) {
	        alert('Error in getting info: ' + msg.status);
	        if(msg.status=='401'){
	        	 var url = "http://" + $window.location.host + "/login";
	             $window.location.href = url;
	        }
	    });
	}
});

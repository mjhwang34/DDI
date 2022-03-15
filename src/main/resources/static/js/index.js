var msg = message[locale];

function ng_confirm(opt, callback) {
    if (opt.showCloseButton == undefined) opt.showCloseButton = false;
    if (opt.showCancelButton == undefined) opt.showCancelButton = true;
    if (opt.focusConfirm == undefined) opt.focusConfirm = false;

    if (opt.confirmButtonText == undefined) opt.confirmButtonText = msg.yes;
    if (opt.cancelButtonText == undefined) opt.cancelButtonText = msg.no;

    Swal.fire(opt).then((result) => {
        if (callback != undefined) callback(result);
    })
}

function ng_alert(opt, callback) {
    if (typeof opt == 'string') {
        ng_alert({ text: opt }, callback)
    } else {
        opt.showCancelButton = false;
        ng_confirm(opt, callback)
    }
}

(function (window) {
    'use strict';
    angular
        .module('app')
        .controller('pageCtrl', ['$sce', '$scope', '$timeout', '$q', 'API', '$mdDialog', function ($sce, $scope, $timeout, $q, API, $mdDialog ) {
            var self = this;

            $scope.locale = locale;

            $scope.mode = 'loading';
            $scope.tab = 'dfi';
            $scope.user = {};
            $scope.ctrl = {};

            $scope.dfiForm_is_valid = true;
            $scope.show_dfi_list = false;

            $scope.dfi_mode = 'fd';

            $scope.drug_seq = 0;
            $scope.food_seq = 0;

            $scope.list_drug = [];
            $scope.list_dfi = [];

            $scope.item_dfi = {
                pmid: ''
            }

            $scope.is_first_ddi = true;
            $scope.is_first_dfi = true;

            $scope.show_logo = false;

            function chk_error(err) {
                console.error(err);
                if (err.status == '403') {
                    $scope.login_message = msg.need_to_login;
                    $scope.mode = 'login';
                } else {

                }
            }

            $timeout(function () {
                API.user.is_login({}).$promise.then(function (response) {
                    $scope.mode = 'ddi';
                }).catch(err => {
                    $scope.mode = 'login';
                })
            }, 1000);

            $scope.login = function (ev) {
				//API.user.login({passwd:$scope.user.passwd}).$promise.then(function(response){
                API.user.login({}, $.param({ passwd: $scope.user.passwd })).$promise.then(function (response) {
                    $scope.mode = 'ddi';
                    $scope.user.passwd = '';
                }).catch(err => {
                    ng_alert(msg.no_match_password)
                })
            }


            $scope.onTabSelected = function (n) {
                if (n == 'ddi') {
                    if($scope.is_first_ddi) $scope.show_logo = false;
                    else $scope.show_logo = true;
                } else if (n == 'dfi') {
                    if($scope.is_first_dfi) $scope.show_logo = false;
                    else $scope.show_logo = true;
                }
            }


            // DFI 시작 ==========================================================

            $scope.init_dfi = function () {
                $scope.dfiForm_is_valid = false;
                $scope.show_dfi_list = false;

                $scope.drug_seq = 0;
                $scope.food_seq = 0;

                $scope.list_drug = [];
                $scope.list_dfi = [];

                $scope.dfi_item = {
                    pmid: ''
                }
                this.selected_drug = {};
            }

            $scope.searchFood = function (query) {
                $scope.init_dfi();
                if (query.length > 2) {
                    return run_search_api(API.dfi.foods({ keyword: query }))
                } else {
                    return search([], query)
                }
            }

            $scope.selectedFood = function (item) {
                if (item) {
                    $scope.food_seq = item.value;
                    this.selected_drug = {};
                    API.dfi.drugs({ food_seq: $scope.food_seq }).$promise.then(rs => {
                        $scope.list_drug = [];
                        if (rs.data) {
                            rs.data.forEach(item => {
                                $scope.list_drug.push({ value: item.seq, display: item.name })
                            })
                        }
                    }).catch(err => {
                        chk_error(err);
                    })
                } else {
                    $scope.list_drug = [];
                }
            }

            $scope.selectDrug = function () {
                $scope.drug_seq = this.selected_drug.value;
                $scope.dfiForm_is_valid = true;
            }


            $scope.searchDrug = function (query) {
                $scope.init_dfi();
                if (query.length > 2) {
                    return run_search_api(API.dfi.drugs({ keyword: query }))
                } else {
                    return search([], query)
                }
            }

            $scope.selectedDrug = function (item) {
                if (item) {
                    $scope.drug_seq = item.value;
                    this.selected_drug = {};
                    API.dfi.foods({ drug_seq: $scope.drug_seq }).$promise.then(rs => {
                        $scope.list_food = [];
                        if (rs.data) {
                            rs.data.forEach(item => {
                                $scope.list_food.push({ value: item.seq, display: item.name })
                            })
                        }
                    }).catch(err => {
                        chk_error(err);
                    })
                } else {
                    $scope.list_food = [];
                }
            }

            $scope.selectFood = function () {
                $scope.food_seq = this.selected_food.value;
                $scope.dfiForm_is_valid = true;
            }


            $scope.dfi = function (ev) {
                $scope.dfi_list = [];
                $scope.is_first_dfi = false;
                API.dfi.search({ drug_seq: $scope.drug_seq, food_seq: $scope.food_seq }).$promise.then(rs => {
	console.log(rs)
                    $scope.list_dfi = rs.data;
                    if ($scope.list_dfi.length == 1) {
                        $scope.show_dfi_list = false;
                        $scope.dfi_detail($scope.list_dfi[0])
                    } else {
                        $scope.show_dfi_list = true;
                        $scope.dfi_detail($scope.list_dfi[0])
                    }
                    $scope.show_logo = true;
                }).catch(err => {
                    chk_error(err);
                })
            }

            $scope.dfi_detail = function (item) {
	console.log(item)
                $scope.item_dfi = item;
                washing_item($scope.item_dfi)
            }
            // DFI 종료 ==========================================================


            $scope.switch_dfi = function () {
                if ($scope.dfi_mode == 'df') $scope.dfi_mode = 'fd';
                else $scope.dfi_mode = 'df';
            }


            // DDI 시작 ==========================================================
            $scope.init_ddi = function () {
                $scope.pdrug_n = null;
                $scope.pdrug_name = 'Prepetrator';

                $scope.vdrug_n = 0;
                $scope.vdrug_name = 'Victim';

                $scope.proteins = null;
            }

            $scope.searchPDrug = function (query) {
                if (query.length > 2) {
                    return run_search_api(API.ddi.drugs({ keyword: query }))
                } else {
                    return search([], query)
                }
            }

            $scope.searchVDrug = function (query) {
                if (query.length > 2) {
                    return run_search_api(API.ddi.drugs({ keyword: query }))
                } else {
                    return search([], query)
                }
            }


            $scope.selectedPDrug = function (item) {
                if (item) {
                    $scope.pdrug_n = item.value;
                    $scope.pdrug_name = item.display;

                    $scope.selectedItem1 = {
                        display: $scope.pdrug_name
                    }
                }
            }

            $scope.selectedVDrug = function (item) {
                if (item) {
                    $scope.vdrug_n = item.value;
                    $scope.vdrug_name = item.display;

                    $scope.selectedItem2 = {
                        display: $scope.vdrug_name
                    }
                }
            }

            function setSubstitution1(item) {
                var pk = {};
                pk.prepetrator = item.prepetrator_name;
                pk.victim = item.victim_name;
                pk.value = Number(item.fold) - 1;
                pk.value_p = Math.abs(pk.value * 100);
                pk.value_p = Number(pk.value_p.toFixed(2));
                pk.reference = item.reference;

                if (item.reference == 1) pk.ref = '인공지능 예측결과 입니다.';
                else if (item.reference == 0) pk.ref = 'FDA 약품설명서에서 추출한 결과입니다.';

                pk.detail = `${pk.prepetrator}은 ${pk.victim}의 AUC를 ${pk.value_p}%만큼 `;
                if (pk.value > 0) pk.detail += '증가시킴.';
                else if (pk.value < 0) pk.detail += '감소시킴.';
                else pk.detail = `${pk.prepetrator}은 ${pk.victim}의 AUC에 영향을 주지 않음.`;

                return pk;
            }


            function setSubstitution(item) {
                var pk = {};
                pk.prepetrator = item.prepetrator_name;
                pk.victim = item.victim_name;
                pk.value = item.value;
                pk.value_p = item.value_p;
                pk.reference = item.reference;
                pk.detail = item.detail;
                pk.ref = item.ref;
                return pk;
            }


            $scope.ddi = function (ev) {
                $scope.loading_ddi_1 = true;
                $scope.loading_ddi_2 = true;
                $scope.loading_ddi_3 = true;
                $scope.loading_ddi_4 = true;
                $scope.loading_ddi_5 = true;
                $scope.is_first_ddi = false;
                API.ddi.pk({ pdrug: $scope.pdrug_n, vdrug: $scope.vdrug_n }).$promise.then(rs => {
                    $scope.pks = [];
                    if (rs.data) {
                        rs.data.forEach(item => {
                            $scope.pks.push(setSubstitution(item));
                        });
                    }
                    $scope.loading_ddi_1 = false;
                    $scope.show_logo = true;

                    API.ddi.protein({ pdrug: $scope.pdrug_n, vdrug: $scope.vdrug_n }).$promise.then(rs2 => {
                        $scope.proteins = rs2.data;
                        $scope.loading_ddi_2 = false;
                    });

                    API.ddi.gene({ vdrug: $scope.vdrug_n }).$promise.then(rs2 => {
                        $scope.genes = rs2.data;
                        $scope.loading_ddi_3 = false;

                        $scope.genes.forEach(item=>{
                            if(item.alt_freq.endsWith(',')) {
                                item.alt_freq = item.alt_freq.replace(',','');
                            }
                        })
                    });

                    $scope.filter1 = {
                        is_all: true,
                        list: [],
                        db: {}
                    };
                    $scope.filter2 = {
                        is_all: true,
                        list: [],
                        db: {}
                    };

                    $scope.filter3 = {
                        is_all: true,
                        list: [{c:true, l:msg.increase},{c:true,l:msg.no_affect},{c:true, l:msg.decrease}],
                        db: {}
                    };

                    $scope.filter4 = {
                        is_all: true,
                        list: [{c:true, l:msg.ref_fda},{c:true,l:msg.ref_ai}],
                        db: {}
                    };

                    API.ddi.substitution({ pdrug: $scope.pdrug_n, vdrug: $scope.vdrug_n }).$promise.then(rs2 => {
                        var list = [];
                        rs2.data.forEach(item => {
                            var item2 = setSubstitution(item)
                            list.push(item2);

                            if (!$scope.filter1.db[item2.prepetrator]) {
                                $scope.filter1.db[item2.prepetrator] = 1
                                $scope.filter1.list.push({ l: item2.prepetrator, c: true })
                            }

                            if (!$scope.filter2.db[item2.victim]) {
                                $scope.filter2.db[item2.victim] = 1
                                $scope.filter2.list.push({ l: item2.victim, c: true })
                            }
                        });

                        $scope.filter1.list.sort(function (a, b) {
                            if (a.l < b.l) { return -1; }
                            if (a.l > b.l) { return 1; }
                            return 0;
                        })

                        $scope.filter2.list.sort(function (a, b) {
                            if (a.l < b.l) { return -1; }
                            if (a.l > b.l) { return 1; }
                            return 0;
                        })

                        $scope.substitutions = list;
                        $scope.loading_ddi_4 = false;
                    });

                    //API.ddi.network({ pdrug: $scope.pdrug_n, vdrug: $scope.vdrug_n }).$promise.then(rs2 => {
                      //  $scope.networks = rs2.data;
                    //});

                }).catch(err => {
                    chk_error(err);
                })
            }
			
            $scope.showNet = function (ev) {
                $scope.mode = 'network';

                $timeout(function () {
                    $scope.drawNet(ev);
                }, 100);
            }

            $scope.closeNet = function (ev) {
                d3.select('svg').remove();
                $scope.mode = 'ddi';
            }
            var graph;
            $scope.drawNet = function (ev) {
                d3.select('svg').remove();
                graph = new drawNet();  
                graph.initialize($scope.networks);
            }

            $scope.sortName = 'value_p';
            $scope.sortReverse = false;

            $scope.sortby = function (sort) {
                if ($scope.sortName == sort) {
                    if ($scope.sortReverse) $scope.sortReverse = false;
                    else $scope.sortReverse = true;
                } else {
                    $scope.sortName = sort;
                    $scope.sortReverse = false;
                }
            }

            var ref_val = 0;
            if(locale == 'eng') ref_val = 1;

            $scope.ddi_filtering = function(item) {
                var is_ok = true;
                if($scope.filter1.is_all) {

                } else {
                    if($scope.filter1.db[item.prepetrator] == undefined) return false;
                }

                if($scope.filter2.is_all) {

                } else {
                    if($scope.filter2.db[item.victim] == undefined) return false;
                }

                if($scope.filter3.is_all) {

                } else {
                    var is_v = false;
                    if($scope.filter3.db[msg.increase] != undefined && item.value > ref_val) is_v = true;
                    if($scope.filter3.db[msg.no_affect] != undefined && item.value == ref_val) is_v = true;
                    if($scope.filter3.db[msg.decrease] != undefined && item.value < ref_val) is_v = true;
                    if(!is_v) return false;
                    //return false;
                }

                if($scope.filter4.is_all) {

                } else {
                    var is_v = false;
                    if($scope.filter4.db[msg.ref_ai] != undefined && item.reference == 1) is_v = true;
                    if($scope.filter4.db[msg.ref_fda] != undefined && item.reference == 0) is_v = true;
                    if(!is_v) return false;

                }
                return true;
            }

            $scope.openFilter1 = function (ev) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/filter.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals:{filter:JSON.parse(JSON.stringify($scope.filter1))},
                    clickOutsideToClose: true,
                    fullscreen: $scope.customFullscreen
                }).then(function (answer) {
                    $scope.filter1 = answer;
                }, function () {

                });
            }

            $scope.openFilter2 = function (ev) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/filter.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals:{filter:JSON.parse(JSON.stringify($scope.filter2))},
                    clickOutsideToClose: true,
                    fullscreen: $scope.customFullscreen
                }).then(function (answer) {
                    $scope.filter2 = answer;
                }, function () {

                });
            }

            $scope.openFilter3 = function (ev) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/filter.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals:{filter:JSON.parse(JSON.stringify($scope.filter3))},
                    clickOutsideToClose: true,
                    fullscreen: $scope.customFullscreen
                }).then(function (answer) {
                    $scope.filter3 = answer;
                }, function () {

                });
            }

            $scope.openFilter4 = function (ev) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/filter.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals:{filter:JSON.parse(JSON.stringify($scope.filter4))},
                    clickOutsideToClose: true,
                    fullscreen: $scope.customFullscreen
                }).then(function (answer) {
                    $scope.filter4 = answer;
                }, function () {

                });
            }

            function DialogController($scope, $mdDialog, filter) {
                $scope.filter = filter;

                $scope.hide = function () {
                    $mdDialog.hide();
                };

                $scope.cancel = function () {
                    $mdDialog.cancel();
                };

                $scope.done = function (answer) {
                    answer.db = {};
                    answer.list.forEach(item=>{
                        if(item.c) answer.db[item.l] = 1;
                    })
                    $mdDialog.hide(answer);
                };

                $scope.selectAll = function(ev) {
                    $scope.filter.list.forEach(item=>{
                        if(!$scope.filter.is_all) item.c = true;
                        else item.c = false;
                    });
                }

                $scope.select = function(ev, item) {
                    if($scope.filter.is_all) $scope.filter.is_all = false;
                }
            }

            $scope.downExcel = function() {
                window.open(`/api/ddi/substitution/excel?pdrug=${$scope.pdrug_n}&vdrug=${$scope.vdrug_n}&lang=${locale}`)
            }

            

            

            // DDI 종료 ==========================================================

            function run_search_api(api) {
                return new Promise((resolve, reject) => {
                    api.$promise.then(rs => {
                        var list = [];
                        rs.data.forEach(item => {
                            list.push({ value: item.n, display: item.name })
                        })
                        resolve(list)
                    }).catch(err => {
                        chk_error(err)
                        reject(err);
                    })
                });
            }

            function search(list, query) {
                var results = query ? list.filter(createFilterFor(query)) : list;
                var deferred = $q.defer();
                $timeout(function () { deferred.resolve(results); }, 10, false);
                return deferred.promise;
            }

            function createFilterFor(query) {
                var lowercaseQuery = query.toLowerCase();
                return function filterFn(state) {
                    return (state.value.startsWith(lowercaseQuery));
                };
            }


            var is_trust_html = false;

            function washing_item(item) {
                var arr1 = washing(item, 'key_sents')
                emphasis(item, arr1, 'key_sents_in_txt')

                var arr2 = washing(item, 'foods')
                emphasis(item, arr2, 'foods_in_txt')

                var arr3 = washing(item, 'drugs')
                emphasis(item, arr3, 'drugs_in_txt')

                if (!is_trust_html) {
                    item.abstract = $sce.trustAsHtml(item.abstract);
                    is_trust_html = true;
                }

            }

            function emphasis(item, arr, className) {
                if (arr != null) {
                    arr.forEach(key => {
                        var txt = item.abstract;
                        item.abstract = txt.replaceAll(key, "<span class='" + className + "'>" + key + "</span>");
                    });
                }
            }

            function washing(item, name) {
                var str = item[name];
                item['arr_' + name] = [];
                if (str != undefined && str != null && str.length > 2 && str.startsWith('[') && str.endsWith(']')) {
                    str = str.trim();
                    str = str.substring(1, str.length - 1); // [] 제거

                    var regex = new RegExp("', '", 'gi');
                    str = str.replace(regex, "','")

                    item[name] = str;

                    var arr = str.split("','")
                    arr.forEach((str, i) => {
                        arr[i] = washingTxt(str)
                    })
                    return arr;
                } else {
                    return null;
                }
            }


            function washingTxt(str) {
                if (str != undefined && str != null && str.length > 2) {
                    if (str.startsWith("'")) str = str.substring(1);
                    if (str.endsWith("'")) str = str.substring(0, str.length - 1);
                    return str;
                } else {
                    return str;
                }
            }

            $scope.init_ddi();

            var legends = [];
            Object.keys(label_path[locale]).forEach(k => {
				//console.log("여기");
				//console.log($scope.mode);
				//console.log(label_path[locale]);
				//console.log(locale);
                legends.push({ name: k, fullname: label_path[locale][k], color: colors_path[k] });
            })

            $scope.legends = legends;

			// Change Password ===================================================================
			
			$scope.changePassword = function (ev) {
				API.user.change_password({}, $.param({curPasswd: $scope.user.curPasswd, newPasswd: $scope.user.newPasswd})).$promise.then(function(response){
                    $scope.mode = 'ddi';
                    $scope.user.curPasswd = '';
					$scope.user.newPasswd = '';
					ng_alert(msg.password_changed);
                }).catch(err => {
                    ng_alert(msg.no_match_password)
                })
            }
			
			// Currency =====================================================================

            $scope.limitOptions = [10, 30, 60];

            $scope.query = {
                order: 'code',
                limit: 10,
                currentPage: 1
            };

            $scope.options = {
                limitSelect: true,
                pageSelect: true
            }

			$scope.detailMode=false;
			$scope.modeCurrency=null;

            $scope.list = function() {
                $scope.loading =  API.currency.list({}).$promise;

                $scope.loading.then(response=>{
                    $scope.currencyList = response.data;
                    $scope.currencyListCount = response.data.length;
                })
			}

			$scope.selectCurrency = function (currency, indx) {
                $scope.selectedCurrencyInfo = JSON.parse(JSON.stringify(currency));
                $scope.selected_currency_index = indx;
				$scope.detailMode=true;
				$scope.modeCurrency="select";
				console.log($scope.detailMode);
            }

            $scope.insert = function(){
				$scope.selectedCurrencyInfo={
                    code:"",
                    num:null,
                    memo:""
                }
				$scope.detailMode=true;
				$scope.modeCurrency="add";
            }

            $scope.list();

			$scope.close = function () {
				$scope.detailMode=false;
            }

            $scope.modify = function () {
				$scope.modeCurrency="edit";
            }

            $scope.save = function () {
				if($scope.modeCurrency=="add"){
					API.currency.add({},$scope.selectedCurrencyInfo).$promise.then(response=>{
                        $scope.list();
                        $scope.close();
                    }).catch(err=>{
                        console.error(err);
                    })
				}
				else if($scope.modeCurrency=="edit"){
					API.currency.update({num:$scope.selectedCurrencyInfo.num}, {memo:$scope.selectedCurrencyInfo.memo}).$promise.then(response=>{
                        $scope.list();
                    }).catch(err=>{
                        console.error(err);
                    })
				}
            }

            $scope.delete = function(){
                if(confirm("정말 삭제하시겠습니까?")){
                    API.currency.delete({num:$scope.selectedCurrencyInfo.num}).$promise.then(response=>{
                        $scope.list();
                        $scope.close();
                    }).catch(err=>{
                        console.error(err);
                    })
                }
            }

            $scope.reset = function () {
                API.currency.get({num:$scope.selectedCurrencyInfo.num}).$promise.then(response=>{
                    $scope.selectedCurrencyInfo = response.data;
                })
                $scope.selectedCurrencyInfo = JSON.parse(JSON.stringify($scope.currencyList[$scope.selected_currency_index]));
            }
        }]);
})(window);
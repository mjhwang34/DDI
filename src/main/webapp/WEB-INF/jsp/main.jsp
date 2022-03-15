<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="app">

<head>
    <meta charset="utf-8">
    <meta name="viewport"
        content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <title>DDIP-NNP (Drug-Drug Interaction Prediction Neural Network Platform)</title>
    <!-- Angular Material CSS now available via Google CDN; version 1.2.1 used here -->
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.1/angular-material.min.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="/css/ddi.css">
    <link rel="stylesheet" href="/css/ddi_ko.css">
    <link rel="stylesheet" href="/css/loading.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/angular-material-data-table/0.10.10/md-data-table.min.css">
</head>

<body>
    <div ng-controller="pageCtrl as ctrl" layout="column" ng-cloak class="md-inline-form h100">
        <md-content>
            <md-card ng-if="mode=='loading'">
                <div class="overlay">
                    <div class="loader"></div>
                </div>
            </md-card>
            <md-card ng-if="mode=='login'">
                <div style="margin: auto;max-width: 80%;">
                    <img src="/images/logo.png"
                        style="max-width: 400px;width:100%;margin-bottom: 30px;" />
                    <form name="loginForm" style="max-width: 400px;margin: auto;">
                        <md-input-container class="md-block">
                            <label>{{ 'password' | translate }}</label>
                            <input type="text" required md-no-asterisk name="passwd" ng-model="user.passwd">
                            <div ng-messages="loginForm.passwd.$error">
                                <div ng-message="required">{{ 'input_password' | translate }}</div>
                            </div>
                        </md-input-container>
                        <md-button class="md-raised md-primary" style="margin:0px;width:100%"
                            ng-disabled="loginForm.$invalid" ng-click="login($event)">{{ 'login' | translate }}</md-button>
                        <span class='login_message'>{{login_message}}</span>
                    </form>
                </div>
            </md-card>
            <md-card ng-if="mode=='ddi'" class="ddi_dfi">
                <div ng-if="show_logo" style="position: absolute;top: 10px;right: 10px;z-index: 1000;background-color: white;padding: 20px;"><img src="/images/logo.png"
                    style="height:136px;"/></div>
                <md-tabs md-dynamic-height md-border-bottom>

                    <md-tab label="{{ 'tab1' | translate }}" md-on-select="onTabSelected('ddi')">
                        <md-content class="md-padding">
                            <div ng-if="is_first_ddi" style="width: 100%; margin:100px 0px 30px;text-align: center;"><img src="/images/logo.png" style="max-width: 400px;width:100%;margin: 0px auto;" /></div>
                            <form name="ddiForm">
                                <div ng-if="is_first_ddi" layout="row" style="background-color: white;margin:0px auto;width:fit-content" class="md-padding" >
                                    <md-autocomplete flex required md-input-name="pdrug" md-min-length="3"
                                        style="max-width: 300px;width: 400px;" md-input-maxlength="180" md-no-cache="ctrl.noCache"
                                        md-selected-item="selectedItem1" md-search-text="pdrug"
                                        md-items="item in searchPDrug(pdrug)" md-item-text="item.display"
                                        md-escape-options="clear" md-require-match="" 
                                        md-floating-label="{{ 'perpetrator_drug' | translate }}"
                                        md-selected-item-change="selectedPDrug(item)"
                                        input-aria-describedby="favoriteStateDescription">
                                        <md-item-template>
                                            <span md-highlight-text="pdrug">{{item.display}}</span>
                                        </md-item-template>
                                        <md-not-found>
                                            No matches found.
                                        </md-not-found>
                                    </md-autocomplete>
                                    <md-autocomplete flex required md-input-name="vdrug" md-min-length="3"
                                        style="max-width: 300px;width: 400px;" md-input-maxlength="180" md-no-cache="ctrl.noCache"
                                        md-selected-item="selectedItem2" md-search-text="vdrug"
                                        md-items="item in searchVDrug(vdrug)" md-item-text="item.display"
                                        md-escape-options="clear" md-require-match="" md-floating-label="{{ 'victim_drug' | translate }}"
                                        md-selected-item-change="selectedVDrug(item)"
                                        input-aria-describedby="favoriteStateDescription">
                                        <md-item-template>
                                            <span md-highlight-text="vdrug">{{item.display}}</span>
                                        </md-item-template>
                                        <md-not-found>
                                            No matches found.
                                        </md-not-found>
                                    </md-autocomplete>

                                    <md-button class="md-raised md-primary" style="max-width: 200px;height: 50px;"
                                        ng-click="ddi($event)" ng-disabled="ddiForm.$invalid">{{ 'result' | translate }}</md-button>
                                </div>

                                <div ng-if="!is_first_ddi" layout="row" style="background-color: white" class="md-padding" >
                                    <md-autocomplete flex required md-input-name="pdrug" md-min-length="3"
                                        style="max-width: 300px;" md-input-maxlength="180" md-no-cache="ctrl.noCache"
                                        md-selected-item="selectedItem1" md-search-text="pdrug"
                                        md-items="item in searchPDrug(pdrug)" md-item-text="item.display"
                                        md-escape-options="clear" md-require-match=""
                                        md-floating-label="{{ 'perpetrator_drug' | translate }}"
                                        md-selected-item-change="selectedPDrug(item)"
                                        input-aria-describedby="favoriteStateDescription">
                                        <md-item-template>
                                            <span md-highlight-text="pdrug">{{item.display}}</span>
                                        </md-item-template>
                                        <md-not-found>
                                            No matches found.
                                        </md-not-found>
                                    </md-autocomplete>
                                    <md-autocomplete flex required md-input-name="vdrug" md-min-length="3"
                                        style="max-width: 300px;" md-input-maxlength="180" md-no-cache="ctrl.noCache"
                                        md-selected-item="selectedItem2" md-search-text="vdrug"
                                        md-items="item in searchVDrug(vdrug)" md-item-text="item.display"
                                        md-escape-options="clear" md-require-match="" md-floating-label="{{ 'victim_drug' | translate }}"
                                        md-selected-item-change="selectedVDrug(item)"
                                        input-aria-describedby="favoriteStateDescription">
                                        <md-item-template>
                                            <span md-highlight-text="vdrug">{{item.display}}</span>
                                        </md-item-template>
                                        <md-not-found>
                                            No matches found.
                                        </md-not-found>
                                    </md-autocomplete>

                                    <md-button class="md-raised md-primary" style="max-width: 200px;height: 50px;"
                                        ng-click="ddi($event)" ng-disabled="ddiForm.$invalid">{{ 'result' | translate }}</md-button>
                                </div>
                                <div class="ddi_info" ng-if="!is_first_ddi">
                                    <div ng-if="!is_first_ddi">
                                        <span class="tit">{{ 'fold_change_of_auc' | translate }}</span>

                                        <span class="txt" ng-repeat="pk in pks">
                                            {{pk.detail}}
                                            <span ng-if="pk.value != 0"><br>{{ 'guide1' | translate }}</span>
                                        </span>
                                        <span class="txt" ng-show="!loading_ddi_1" ng-if="pks.length == 0">{{ 'no_result' | translate }}</span>
    
                                        <div layout="row" layout-sm="column" layout-align="space-around"
                                            ng-show="loading_ddi_1">
                                            <md-progress-circular md-mode="indeterminate"></md-progress-circular>
                                        </div>
    
                                        <span class="tit">{{ 'reference' | translate }}</span>
                                        <span class="txt" ng-repeat="pk in pks">{{pk.ref}}</span>
                                        <span ng-show="!loading_ddi_1" ng-if="pks.length == 0">{{ 'no_result' | translate }}</span>
    									
                                        <span class="tit">{{ 'interaction_protein_information' | translate }} <md-button class="md-raised md-primary" ng-click="showNet()" ng-if="networks.length>0">{{ 'show_network' | translate }}</md-button></span>
                                        <span class="txt" ng-show="!loading_ddi_2" ng-if="proteins.length > 0">{{ 'guide2' | translate }}</span>
                                        <div layout="row" layout-sm="column" layout-align="space-around"
                                            ng-show="loading_ddi_2">
                                            <md-progress-circular md-mode="indeterminate"></md-progress-circular>
                                        </div>
                                        <span class="txt" ng-show="!loading_ddi_2" ng-if="proteins.length == 0">{{ 'no_result' | translate }}</span>
                                        <table class="protein" ng-show="proteins.length > 0">
                                            <tr>
                                                <th style="width:60%">{{ 'protein' | translate }}</th>
                                                <th style="width:20%">{{pdrug_name}}</th>
                                                <th style="width:20%">{{vdrug_name}}</th>
                                            </tr>
                                            <tr ng-repeat="item in proteins">
                                                <td>{{item.polyname}}</td>
                                                <td>{{item.p}}</td>
                                                <td>{{item.v}}</td>
                                            </tr>
                                        </table>
    
                                        
    
                                        <span class="tit">{{ 'korean_allele_frequency' | translate }}</span>
                                        <div layout="row" layout-sm="column" layout-align="space-around"
                                            ng-show="loading_ddi_3">
                                            <md-progress-circular md-mode="indeterminate"></md-progress-circular>
                                        </div>
                                        <span class="txt" ng-show="!loading_ddi_3" ng-if="genes.length == 0">{{ 'no_result' | translate }}</span>
                                        <div style="width: 100%; overflow-x: auto;">
                                            <table class="gene"  ng-show="genes.length > 0">
                                                <tr>
                                                    <th>{{ 'protein_id' | translate }}</th>
                                                    <th>{{ 'protein_name' | translate }}</th>
                                                    <th>{{ 'genotype' | translate }}</th>
                                                    <th>{{ 'rsname' | translate }}</th>
                                                    <th>{{ 'allele_name' | translate }}</th>
                                                    <th>{{ 'change' | translate }}</th>
                                                    <th>{{ 'description' | translate }}</th>
                                                    <th>{{ 'wild_type' | translate }}</th>
                                                    <th>{{ 'wild_type_allele_frequency' | translate }}</th>
                                                    <th>{{ 'variant_type_allele_frequency' | translate }}</th>
                                                </tr>
                                                <tr ng-repeat="item in genes">
                                                    <td>{{item.protein}}</td>
                                                    <td>{{item.protein_name}}</td>
                                                    <td>{{item.genotype}}</td>
                                                    <td>{{item.rsname}}</td>
                                                    <td>{{item.allele_name}}</td>
                                                    <td>{{item.change}}</td>
                                                    <td>{{item.description}}</td>
                                                    <td>{{item.reference}}</td>
                                                    <td>{{item.ref_freq}}</td>
                                                    <td>{{item.alt_freq}}</td>
                                                </tr>
                                            </table>
                                        </div>
    
                                        <span class="tit">{{ 'recommended_alternative_drug_pairs' | translate }} ({{ 'total' | translate }} : {{filtered.length}}{{ 'recommended_alternative_drug_pairs_suffix' | translate }})
                                            <md-button class="md-raised md-primary" ng-click="downExcel()" ng-if="substitutions.length>0">{{ 'download_excel' | translate }}</md-button></span>
                                        <div layout="row" layout-sm="column" layout-align="space-around"
                                            ng-show="loading_ddi_4">
                                            <md-progress-circular md-mode="indeterminate"></md-progress-circular>
                                        </div>
                                        <span class="txt" ng-show="!loading_ddi_4" ng-if="substitutions.length == 0">{{ 'no_result' | translate }}</span>
                                        <table ng-show="substitutions.length > 0">
                                            <tr>
                                                <th style="width:25%">
                                                    <span class="btn_sort" ng-click="sortby('prepetrator')">{{ 'perpetrator_drug' | translate }}</span>
                                                    <span class="material-icons btn_sort" ng-click="sortby('prepetrator')" ng-if="sortName == 'prepetrator' && sortReverse">
                                                        arrow_circle_down
                                                    </span>
                                                    <span class="material-icons btn_sort" ng-click="sortby('prepetrator')" ng-if="sortName == 'prepetrator' && !sortReverse">
                                                        arrow_circle_up
                                                    </span>  
                                                </th>
                                                <th style="width:25%">
                                                    <span class="btn_text" ng-click="sortby('victim')">{{ 'victim_drug' | translate }}</span>
                                                    <span class="material-icons btn_sort" ng-click="sortby('victim')" ng-if="sortName == 'victim' && sortReverse">
                                                        arrow_circle_down
                                                    </span>
                                                    <span class="material-icons btn_sort" ng-click="sortby('victim')" ng-if="sortName == 'victim' && !sortReverse">
                                                        arrow_circle_up
                                                    </span>  
                                                </th>
                                                <th style="width:25%">
                                                    <span class="btn_text" ng-click="sortby('value_p')">{{ 'interaction' | translate }}</span>
                                                        <span class="material-icons btn_sort" ng-click="sortby('value_p')" ng-if="sortName == 'value_p' && sortReverse">
                                                            arrow_circle_down
                                                        </span>
                                                        <span class="material-icons btn_sort" ng-click="sortby('value_p')" ng-if="sortName == 'value_p' && !sortReverse">
                                                            arrow_circle_up
                                                        </span>  
                                                    </th>

                                                <th style="width:25%">
                                                        <span class="btn_text" ng-click="sortby('reference')">{{ 'reference' | translate }}</span>
                                                            <span class="material-icons btn_sort" ng-click="sortby('reference')" ng-if="sortName == 'reference' && sortReverse">
                                                                arrow_circle_down
                                                            </span>
                                                            <span class="material-icons btn_sort" ng-click="sortby('reference')" ng-if="sortName == 'reference' && !sortReverse">
                                                                arrow_circle_up
                                                            </span>  
                                                        </th>
                                            </tr>
                                            <tr>
                                                <td >
                                                    {{ 'filter' | translate }} : <span id="btn_filter1"></span>
                                                    <span ng-if="filter1.is_all" class="btn_filter" ng-click="openFilter1($event)">{{ 'all' | translate }}</span>
                                                    <span ng-if="!filter1.is_all" class="btn_filter" ng-click="openFilter1($event)">{{ 'part' | translate }}</span>
                                                </td>
                                                <td>
                                                    {{ 'filter' | translate }} : <span id="btn_filter2"></span>
                                                    <span ng-if="filter2.is_all" class="btn_filter" ng-click="openFilter2($event)">{{ 'all' | translate }}</span>
                                                    <span ng-if="!filter2.is_all" class="btn_filter" ng-click="openFilter2($event)">{{ 'part' | translate }}</span>
                                                </td>
                                                <td>
                                                    {{ 'filter' | translate }} : <span id="btn_filter3"></span>
                                                    <span ng-if="filter3.is_all" class="btn_filter" ng-click="openFilter3($event)">{{ 'all' | translate }}</span>
                                                    <span ng-if="!filter3.is_all" class="btn_filter" ng-click="openFilter3($event)">{{ 'part' | translate }}</span>
                                                </td>
                                                <td>
                                                    {{ 'filter' | translate }} : <span id="btn_filter4"></span>
                                                    <span ng-if="filter4.is_all" class="btn_filter" ng-click="openFilter4($event)">{{ 'all' | translate }}</span>
                                                    <span ng-if="!filter4.is_all" class="btn_filter" ng-click="openFilter4($event)">{{ 'part' | translate }}</span>
                                                </td>
                                            </tr>
                                            <tr ng-repeat="item in substitutions | orderBy:sortName:sortReverse | filter:ddi_filtering as filtered">
                                                <td>
                                                    <span ng-if="item.prepetrator ==  pdrug_name" class="key_sents_in_txt">{{item.prepetrator}}</span>
                                                    <span ng-if="item.prepetrator !=  pdrug_name">{{item.prepetrator}}</span>
                                                </td>
                                                <td>
                                                    <span ng-if="item.victim ==  vdrug_name" class="key_sents_in_txt">{{item.victim}}</span>
                                                    <span ng-if="item.victim !=  vdrug_name">{{item.victim}}</span>
                                                </td>
                                                <td>{{item.detail}}</td>
                                                <td>{{item.ref}}</td>
                                            </tr>
                                        </table>
                                    </div>


                                </div>
                            </form>
                        </md-content>
                    </md-tab>


                    <md-tab label="약물-식품 상호작용(Drug-Food Interaction)" md-on-select="onTabSelected('dfi')" ng-if="locale=='ko'">
                        <md-content class="md-padding">
                            <div ng-if="is_first_dfi" style="width: 100%; margin:100px 0px 30px;text-align: center;"><img src="/images/logo.png" style="max-width: 400px;width:100%;margin: 0px auto;" /></div>

                            <div layout="row" style="background-color: white;width: fit-content; margin: auto;" ng-style="{'width':is_first_dfi?'fit-content':'auto'}" class="md-padding">
                                <md-autocomplete flex ng-show="dfi_mode == 'fd'" required md-input-name="food"
                                    md-min-length="3" style="max-width: 200px;" md-input-maxlength="18"
                                    md-no-cache="ctrl.noCache" md-selected-item="selectedItem3" md-search-text="food"
                                    md-items="item in searchFood(food)" md-item-text="item.display"
                                    md-escape-options="clear" md-require-match="" md-floating-label="식품(Food)"
                                    input-aria-describedby="favoriteStateDescription"
                                    md-selected-item-change="selectedFood(item)" placeholder="선택해주세요.">
                                    <md-item-template>
                                        <span md-highlight-text="food">{{item.display}}</span>
                                    </md-item-template>
                                    <md-not-found>
                                        No matches found.
                                    </md-not-found>
                                </md-autocomplete>
                                <md-autocomplete flex ng-show="dfi_mode == 'df'" required md-input-name="drug"
                                    md-min-length="3" style="max-width: 200px;" md-input-maxlength="18"
                                    md-no-cache="ctrl.noCache" md-selected-item="selectedItem4" md-search-text="drug"
                                    md-items="item in searchDrug(drug)" md-item-text="item.display"
                                    md-escape-options="clear" md-require-match="" md-floating-label="약물(Drug)"
                                    input-aria-describedby="favoriteStateDescription"
                                    md-selected-item-change="selectedDrug(item)" placeholder="선택해주세요.">
                                    <md-item-template>
                                        <span md-highlight-text="drug">{{item.display}}</span>
                                    </md-item-template>
                                    <md-not-found>
                                        No matches found.
                                    </md-not-found>
                                </md-autocomplete>

                                <button class="switch" ng-click="switch_dfi()"></button>
                                <md-input-container flex ng-show="dfi_mode == 'fd'" style="min-width: 200px;">
                                    <label>약물(Drug)</label>
                                    <md-select ng-model="selected_drug" required ng-change="selectDrug()"
                                        ng-disabled="list_drug.length == 0"
                                        ng-model-options="{ trackBy: '$value.display' }" placeholder="선택해주세요.">
                                        <md-option ng-repeat="item in list_drug" ng-value="item">
                                            {{item.display}}
                                        </md-option>
                                    </md-select>
                                </md-input-container>

                                <md-input-container flex ng-show="dfi_mode == 'df'" style="min-width: 200px;">
                                    <label>식품(Food)</label>
                                    <md-select ng-model="selected_food" required ng-change="selectFood()"
                                        ng-disabled="list_food.length == 0"
                                        ng-model-options="{ trackBy: '$value.display' }" placeholder="선택해주세요.">
                                        <md-option ng-repeat="item in list_food" ng-value="item">
                                            {{item.display}}
                                        </md-option>
                                    </md-select>
                                </md-input-container>

                                <md-button class="md-raised md-primary" style="max-width: 200px;height: 50px;"
                                    ng-click="dfi($event)" ng-disabled="dfiForm_is_valid == false">결과보기</md-button>

                            </div>

                            <div class="ddi_info" ng-if="!is_first_dfi">
                                <table width="100%" class="list" ng-show="show_dfi_list">
                                    <tr>
                                        <th class='pmid'>Pubmed PMID 번호</th>
                                        <th>논문제목</th>
                                    </tr>
                                    <tr ng-repeat="item in list_dfi" ng-click="dfi_detail(item)">
                                        <td>{{item.pmid}}</td>
                                        <td>{{item.title}}</td>
                                    </tr>
                                </table>
                                <div ng-if="!is_first_dfi">
                                    <span class="tit">Pubmed PMID 번호</span>
                                    <span class="txt">
                                        {{item_dfi.pmid}}
                                        <a href="https://pubmed.ncbi.nlm.nih.gov/{{item_dfi.pmid}}" target="_blank"
                                            ng-show="item_dfi.pmid != ''">( 클릭 )</a>
                                    </span>
    
                                    <span class="tit">논문제목</span>
                                    <span class="txt">{{item_dfi.title}}</span>
    
                                    <span class="tit">주요문장</span>
                                    <span class="txt">{{item_dfi.key_sents}}</span>
    
                                    <span class="tit">초록</span>
                                    <span class="txt" ng-bind-html="item_dfi.abstract"></span>
                                </div>

                            </div>
                        </md-content>
                    </md-tab>
                    <md-tab label="비밀번호 변경(CHANGE PASSWORD)">
                        <md-content class="md-padding">
                            <div layout="row" style="background-color: white;width: fit-content; margin: auto;" ng-style="{'width':is_first_dfi?'fit-content':'auto'}" class="md-padding">
                                <form name="changePasswordForm" style="max-width: 400px;margin: auto;">
                        			<md-input-container class="md-block">
                            			<label>{{ 'current_password' | translate }}</label>
                            			<input type="text" required md-no-asterisk name="curPasswd" ng-model="user.curPasswd">
                            			<div ng-messages="chagnePasswordForm.curPasswd.$error">
                                		<div ng-message="required">{{ 'input_password' | translate }}</div>
                            			</div>
                        			</md-input-container>
                        			<md-input-container class="md-block">
                            			<label>{{ 'new_password' | translate }}</label>
                            			<input type="text" required md-no-asterisk name="newPasswd" ng-model="user.newPasswd">
                            			<div ng-messages="changePasswordForm.newPasswd.$error">
                                		<div ng-message="required">{{ 'input_password' | translate }}</div>
                            			</div>
                        			</md-input-container>
                        			<md-button class="md-raised md-primary" style="margin:0px;width:100%"
                            		ng-disabled="changePasswordForm.$invalid" ng-click="changePassword($event)">{{ 'ok' | translate }}</md-button>
                        			<span class='login_message'>{{login_message}}</span>
                    			</form>
                            </div>
                        </md-content>
                    </md-tab>
                    <md-tab label="통화(Currency)">
                        <md-content class="md-padding">
                            <div layout="row" layout-align="center">
   								<div flex="50">
       								<md-card>
           								<md-table-container>
               								<table md-table class="currency_list" md-progress="loading">
							                	<thead md-head md-order="query.order">
							                    	<tr md-row>
							                        	<th md-column md-order-by="code">Code</th>
							                          	<th md-column md-order-by="num">Num</th>
							                          	<th md-column md-order-by="memo">Memo</th>
							                      	</tr>
							                  	</thead>
							                  	<tbody md-body>
							                    	<tr md-row ng-repeat="data in currencyList | limitTo: query.limit : (query.currentPage -1) * query.limit | orderBy:query.order" md-row-select="false"
							                    	ng-click="selectCurrency(data, $index)">
							                        	<td md-cell class="code">
							                              {{data.code}}
							                          	</td>
							                          	<td md-cell class="num">
							                              {{data.num}}
							                          	</td>
							                          	<td md-cell class="memo">
							                              {{data.memo}}
							                          	</td>
							                      	</tr>
							                  	</tbody>
							              	</table>
							          	</md-table-container>
							          	<md-table-pagination md-limit="query.limit" md-limit-options="limitOptions" md-page="query.currentPage"
							              md-total="{{currencyListCount}}" md-on-paginate="currencyList" md-page-select="options.pageSelect">
							          	</md-table-pagination>
							          	<md-button class="md-raised md-primary" ng-click="insert()">추가</md-button>
							    	</md-card>
							 	</div>
							 	<div flex="50">
									<md-card ng-if="detailMode">
									    <md-card>
									        <md-content layout="column" layout-padding >
									            <div layout="row" layout-align="center">
									              <md-input-container class="md-block" flex="100">
									                <label>Code</label>
									                <input ng-model="selectedCurrencyInfo.code" ng-readonly="modeCurrency!='add'">
									              </md-input-container>
									            </div>
									        	<div layout="row" layout-align="center">
									              <md-input-container class="md-block" flex="100">
									                <label>Num</label>
									                <input ng-model="selectedCurrencyInfo.num" ng-readonly="modeCurrency!='add'">
									              </md-input-container>
									            </div>
												<div layout="row" layout-align="center">
									              <md-input-container class="md-block" flex="100">
									                <label>Memo</label>
									                <input ng-model="selectedCurrencyInfo.memo" ng-readonly="modeCurrency=='select'">
									              </md-input-container>
									            </div>
									         </md-content>
									    </md-card>
									    <div ng-if="modeCurrency=='add'" layout="row">
									    	<div layout="row" flex="50">
									    		<md-button class="md-warn md-raised" ng-click="save()" flex="100">저장</md-button>
									    	</div>
									    	<div layout="row" flex="50">
									    		<md-button class="md-primary" ng-click="close()" flex="100">닫기</md-button>
									    	</div>
									    </div>
									    <div ng-if="modeCurrency=='select'" layout="row">
									    	<div layout="row" flex="33">
									    		<md-button class="md-primary md-raised" ng-click="modify()" flex="100">수정</md-button>
									    	</div>
									    	<div layout="row" flex="33">
									    		<md-button class="md-warn md-raised" ng-click="delete()" flex="100">삭제</md-button>
									    	</div>
									    	<div layout="row" flex="33">
									    		<md-button class="md-primary" ng-click="close()" flex="100">닫기</md-button>
									    	</div>
									    </div>
									    <div ng-if="modeCurrency=='edit'">
									    	<div layout="row" flex="33">
									    		<md-button class="md-warn md-raised" ng-click="save()" flex="100">저장</md-button>
									    	</div>
									    	<div layout="row" flex="33">
									    		<md-button class="md-primary md-raised" ng-click="reset()" flex="100">초기화</md-button>
									    	</div>
									    	<div layout="row" flex="33">
									    		<md-button class="md-primary" ng-click="close()" flex="100">닫기</md-button>
									    	</div>
									    </div>
									</md-card>
							  	</div>
							  	<!--
							 	<div flex="50">
									<md-card ng-if="detailMode">
									    <md-card>
									        <md-content layout="column" layout-padding>
									            <div>
									              <md-input-container class="md-block">
									                <label>Code</label>
									                <input ng-model="selectedCurrencyInfo.code" ng-readonly="modeAdd">
									              </md-input-container>
									        
									              <md-input-container class="md-block">
									                <label>Num</label>
									                <input ng-model="selectedCurrencyInfo.num" ng-readonly="modeAdd">
									              </md-input-container>
									
									              <md-input-container class="md-block">
									                <label>Memo</label>
									                <input ng-model="selectedCurrencyInfo.memo" ng-readonly="modeCurrency">
									              </md-input-container>
									            </div>
									         </md-content>
									    </md-card>
									    <md-button ng-if="!modeCurrency" class="md-warn md-raised" ng-click="save()">저장</md-button>
									    <md-button ng-if="modeCurrency" class="md-primary md-raised" ng-click="modify()">수정</md-button>
									    <md-button ng-if="(!modeCurrency)&&modeAdd" class="md-primary md-raised" ng-click="reset()">초기화</md-button>
									    <md-button ng-if="modeCurrency" class="md-warn md-raised" ng-click="delete()">삭제</md-button>
									    <md-button class="md-primary" ng-click="close()">닫기</md-button>
									</md-card>
							  	</div>-->
							</div>
                        </md-content>
                    </md-tab>
                </md-tabs>
            </md-card>
            <md-card ng-if="mode=='network'" class="network">
                <div id="net"></div>
                <div style="position: absolute;top: 10px;right: 10px;">
                    <md-button class="md-raised md-primary" ng-click="drawNet()">{{ 'redraw' | translate }}</md-button><md-button class="md-raised" ng-click="closeNet()">{{ 'close' | translate }}</md-button>
                </div>

                <div style="position: absolute;top: 10px;left: 10px;">
                    <table class="legend">
                        <tr ng-repeat="item in legends">
                            <td ng-style="{'background-color': item.color};"></td>
                            <td ng-style="{'color':item.color}">{{item.name}}</td>
                            <td ng-style="{'color':item.color}">{{item.fullname}}</td>
                        </tr>
                    </table>
                </div>
            </md-card>
        </md-content>
    </div>
</body>
<script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"
    integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-animate.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-resource.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-route.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-sanitize.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-aria.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular-messages.min.js"></script>

<script src="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.3/angular-material.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.26/angular-ui-router.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-translate/2.18.2/angular-translate.min.js"></script>

<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-material-data-table/0.10.10/md-data-table.min.js"></script>

<script>
    var locale = "${language}";
</script>
<script>
    (function (window) {
        angular.module('commons', ['ngResource', 'ngMaterial', 'ngSanitize']);
        angular.module('app', ['commons']);
    })(window);
</script>
<script src="/js/messages.js"></script>
<script src="/js/api.js"></script>
<script src="/js/config.js"></script>
<script src="/js/service.js"></script>
<script src="/js/draw.net.js"></script>
<script src="/js/index.js"></script>

</html>
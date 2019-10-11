var auctionRegisterView = Vue.component('AuctionRegisterView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 등록하기" description="새로운 경매를 등록합니다."></v-breadcrumb>
            <div class="row">
                <div class="col-md-6 mx-auto">
                    <div class="card">
                        <div class="card-header">신규 경매 등록하기</div>
                        <div class="card-body">
                            <div v-if="!registered">
                                <div class="form-group">
                                    <label id="privateKey">지갑 개인키</label>
                                    <input id="privateKey" v-model="before.input.privateKey" type="text" class="form-control" placeholder="지갑 개인키를 입력해주세요.">
                                </div>
                                <div class="form-group">
                                    <label id="work">작품 선택</label>
                                    <select v-model="before.selectedWork" class="form-control">
                                        <option v-for="work in before.works" :value="work.id">{{ work['이름'] }}</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label id="minPrice">최저가</label>
                                    <div class="input-group">
                                        <input id="minPrice" v-model="before.input.minPrice" type="text" class="form-control" placeholder="최저가를 입력해주세요.">
                                        <div class="input-group-append">
                                            <div class="input-group-text">ETH</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label id="startDate">경매 시작일시</label>
                                    <input id="startDate" v-model="before.input.startDate" type="text" class="form-control" placeholder="yyyy-MM-dd HH:mm:ss, 예: 2019-04-21 21:00:00">
                                </div>
                                <div class="form-group">
                                    <label id="untilDate">경매 종료일시</label>
                                    <input id="untilDate" v-model="before.input.untilDate" type="text" class="form-control" placeholder="yyyy-MM-dd HH:mm:ss, 예: 2019-05-03 12:00:00">
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <button class="btn btn-sm  btn-warning" v-on:click="register" v-bind:disabled="isCreatingContract">{{ isCreatingContract ? "계약을 생성하는 중입니다." : "경매 등록하기" }}</button>
                                    </div>
                                    <div class="col-md-6 text-right">
                                        <button class="btn btn-sm btn-outline-secondary" v-on:click="goBack">이전으로 돌아가기</button>
                                    </div>
                                </div>
                            </div>
                            <div v-if="registered">
                                <div class="alert alert-success" role="alert">
                                    경매가 생성되었습니다.
                                </div>
                                <table class="table table-bordered mt-5">
                                    <tr>
                                        <th>경매작품</th>
                                        <td>{{ after.work['이름'] }}</td>
                                    </tr>
                                    <tr>
                                        <th>최저가</th>
                                        <td>{{ after.result['최저가'] }} ETH</td>
                                    </tr>
                                    <tr>
                                        <th>시작일시</th>
                                        <td>{{ before.input.startDate }}</td>
                                    </tr>
                                    <tr>
                                        <th>종료일시</th>
                                        <td>{{ before.input.untilDate }}</td>
                                    </tr>
                                    <tr>
                                        <th>컨트랙트 주소</th>
                                        <td>{{ after.result['컨트랙트주소'] }}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            isCreatingContract:false,
            registered: false,
            sharedStates: store.state,
            before: {
                works: [],
                selectedWork: null,
                input: {

                }
            },
            after: {
                result: {},
                work: {}
            }
        }
    },
    methods: {
        goBack: function(){
            this.$router.go(-1);
        },
        register: function(){
            var scope = this;
            this.isCreatingContract = true;
            walletService.findAddressById(this.sharedStates.user.id, function(walletAddress){
                createAuction({
                    workId: scope.before.selectedWork,
                    minValue: scope.before.input.minPrice,
                    startTime: new Date(scope.before.input.startDate).getTime()+32400000,
                    endTime: new Date(scope.before.input.untilDate).getTime()+32400000
                }, walletAddress, scope.before.input.privateKey, function(log){
                    var contractAddress = log[log.length -1];
                    var startTimes = new Date(scope.before.input.startDate).getTime()+32400000;
                    var endTimes = new Date(scope.before.input.untilDate).getTime()+32400000;
                    startTimes = new Date(startTimes);
                    endTimes = new Date(endTimes);
                    var data = {
                        "경매생성자id": scope.sharedStates.user.id,
                        "경매작품id": scope.before.selectedWork,
                        "시작일시": startTimes,
                        "종료일시": endTimes,
                        "최저가": Number(scope.before.input.minPrice),
                        "컨트랙트주소": contractAddress,
                    }
                    workService.findById(scope.before.selectedWork, function(result){
                        scope.after.work = result;
                    });
                    auctionService.register(data, function(result){
                      if(result){
                        alert("경매가 등록되었습니다.");
                        scope.registered = true;
                        scope.after.result = data;
                      }
                      else {
                        alert("등록이 실패되었습니다")
                      }
                    });
                    this.isCreatingContract = false;
                });
            });
        }
    },
    mounted: function(){
        var scope = this;
        workService.findWorksByOwner(this.sharedStates.user.id, function(result){
            scope.before.works = result;
        });
    }
})

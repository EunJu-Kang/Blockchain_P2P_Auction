var walletInfoView = Vue.component('walletInfoView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="마이페이지" description="지갑을 생성하거나 작품을 업로드 할 수 있습니다."></v-breadcrumb>
            <div class="container">
                <v-mypage-nav></v-mypage-nav>
                <div id="mywallet-info" class="row">
                    <div class="col-md-12 mt-5">
                        <div class="card">
                            <table class="table table-bordered">
                                <tr>
                                    <th>총보유 ETH</th>
                                    <td class="text-right">{{ wallet['잔액'] }} ETH</td>
                                    <td colspan="2">
                                        <button type="button" class="btn btn-secondary" v-if="wallet['충전회수'] < 10" v-on:click="charge()" v-bind:disabled="isCharging">{{ isCharging ? "충전중" : "ETH 충전하기"}}</button>
                                        <button type="button" class="btn btn-secondary" v-else v-on:click="charge()" v-bind:disabled="!isCharging">{{"충전회수가 초과하였습니다."}}</button>
                                    </td>
                                </tr>
                                <tr>
                                    <th>내 지갑주소</th>
                                    <td class="text-right">{{ wallet['주소'] }}</td>
                                    <th>충전 회수</th>
                                    <td class="text-right">{{ wallet['충전회수'] }}회</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            wallet: {
                id:0,
                "소유자id":0,
                "주소": "",
                "잔액": 0,
                "충전회수": 0
            },
            isCharging: false,          // 현재 코인을 충전하고 있는 중인지 확인하는 변수
            sharedState: store.state
        }
    },
    methods: {
        // ETH 충전하기
        charge: function(){
            var scope = this;
            scope.isCharging = true;
            if(this.wallet['충전회수'] >= 10){
              scope.isCharging = false;
            } else{
              walletService.chargeEther(this.wallet['주소'], function(response){
                scope.isCharging = false;
                alert("코인이 충전 되었습니다.");
                scope.fetchWalletInfo();
              })
            }
        },
        // 지갑 정보 가져오기
        fetchWalletInfo: async function(){
            var scope = this;
            web3.eth.getAccounts().then(res=>{
              scope.wallet['주소'] = res[0]
            })
            var w = scope.wallet;
            await walletService.findById(sessionStorage.getItem("userID"), function(data){
                // TODO API 호출로 지갑 정보를 가져와 보여줍니다.
                // web3를 사용하여 잔액을 조회해 보는 것도 포함해보도록 합니다.
                w.id = data.id;
                w.소유자id = data.소유자id;
                w.주소 = data.주소;
                w.잔액 = data.잔액;
                w.충전회수 = data.충전회수;
            });
            wallet = w;
        }
    },
    mounted: async function(){
        await this.fetchWalletInfo();
    }
})

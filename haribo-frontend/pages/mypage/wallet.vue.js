var walletCreateView = Vue.component('WalletCreateView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="마이페이지" description="지갑을 생성하거나 작품을 업로드 할 수 있습니다."></v-breadcrumb>
            <div class="container">
                <v-mypage-nav></v-mypage-nav>
                <div id="create-wallet-form" class="row">
                    <div class="col-md-12 mt-5">
                        <div class="card">
                            <div class="card-body">
                                <div v-if="step == 0">
                                    <h5 class="card-title">아직 지갑이 없네요! 지갑을 생성하세요.</h5>
                                    <a href="#" v-on:click="createWallet" class="btn btn-lg btn-outline-secondary">지갑생성하기</a>
                                </div>
                                <div v-if="step == 1">
                                    <div class="alert alert-warning" role="alert">
                                        <strong>경고!</strong>
                                        <p>
                                            1. 지갑 비밀키를 잃어버리지 마세요! 한 번 잃어버리면 복구 할 수 없습니다.<br>
                                            2. 공유하지 마세요! 비밀키가 악위적인 사이트에 노출되면 당신의 자산이 유실될 수 있습니다.<br>
                                            3. 백업을 만들어 두세요! 종이에 적어서 오프라인으로 관리하세요.
                                        </p>
                                    </div>
                                    <hr>
                                    <div class="form-group">
                                        <label><b>지갑 비밀키를 저장하세요!</b></label>
                                        <p>{{ privateKey }}</p>
                                    </div>
                                    <hr>
                                    <div class="form-group">
                                        <label><b>지갑 주소</b></label>
                                        <p>{{ walletAddress }}</p>
                                    </div>
                                    <hr>
                                    <a href="#" v-on:click="saveWallet" class="btn btn-outline-secondary">네, 경고를 모두 숙지 했으며 이제 지갑 정보를 저장하겠습니다.</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            step: 0,
            privateKey: '',
            walletAddress: '',
            sharedState: store.state
        }
    },
    methods: {
        createWallet: function(){
            var wallet = web3.eth.accounts.create();
            this.step = 1;
            this.privateKey = wallet.privateKey;
            this.walletAddress = wallet.address;
        },
        saveWallet: function(){
            var scope = this;

            walletService.registerWallet(
                scope.sharedState.user.id,
                this.walletAddress,
                function(response){
                    alert("지갑 주소가 등록되었습니다.");
                    scope.sharedState.user.hasWallet = true;
                    store.state.user.hasWallet = true;
                    scope.$router.push('/mypage/wallet_info');
                }
            );
        }
    }
});

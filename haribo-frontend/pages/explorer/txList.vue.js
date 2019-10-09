var explorerTxListView = Vue.component('ExplorerTxListView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Transaction Explorer" description="블록체인에서 생성된 거래내역을 보여줍니다."></v-breadcrumb>
            <div class="container">
                <explorer-nav></explorer-nav>
                <div class="row" v-if="transactions.length == 0">
                    <div class="col-md-8 mx-auto">
                        <div class="alert alert-warning">No transaction recorded at. #{{ block && block.number }} blocks</div>
                    </div>
                </div>
                <div class="row">
                    <div id="transactions" class="col-md-8 mx-auto">
                        <div class="card shadow-sm">
                            <div class="card-header">Transactions</div>
                            <div class="card-body">
                                <div class="row tx-info" v-for="item in transactions">
                                    <div class="col-md-2">Tx</div>
                                    <div class="col-md-4">
                                        <router-link :to="{name: 'explorer.tx.detail', params: { hash: item.hash }}" class="tx-number">{{ item.hash | truncate(10) }}</router-link>
                                        <p class="tx-timestamp">{{ item.저장일시 }}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><label class="text-secondary">From</label> <router-link :to="{ name: 'explorer.address.detail', params: { address: item.from }}">{{ item.from | truncate(10) }}</router-link></p>
                                        <p><label class="text-secondary">To</label><router-link :to="{ name: 'explorer.address.detail', params: { address: item.to }}">{{ item.to | truncate(10) }}</router-link></p>
                                    </div>
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
            transactions: [],
            block: {}
        };
    },
    methods: {
        fetchTxes: function () {
            var scope = this;
            etheriumService.recentTrans(function (response) {
                if (response) {
                    var BlockNumber;
                    var tranlen = response.length;
                    console.log(tranlen)
                    if(tranlen<10){
                        for (let i = 0; i < tranlen; i++) {
                            console.log(response);
                            etheriumService.findBlockById(response[i].blockNumber, function (blcokdata) {
                                response[i].저장일시 = etheriumService.timeSince(blcokdata.timestamp);
                            })
                        }
                        scope.transactions = response
                    }else{
                    for (let i = 0; i < 2; i++) {
                        console.log(response);
                        etheriumService.findBlockById(response[i].blockNumber, function (blcokdata) {
                                response[i].저장일시 = etheriumService.timeSince(blcokdata.timestamp);
                        })
                    }
                        scope.transactions = response
                }
                }
            })
        }
    },
    mounted: function () {
        this.fetchTxes();

        this.$nextTick(function () {
            window.setInterval(() => {
                this.fetchTxes();
            }, REFRESH_TIMES_OF_TRANSACTIONS);
        })
    }
})

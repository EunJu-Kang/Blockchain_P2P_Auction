var exploreraddressDetailView = Vue.component('ExploreraddressDetailView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Address Explorer" description="검색한 주소의 결과를 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
            <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header">Address <strong> #{{this.address}}</strong></div>
                <table class="table">
                    <tbody>
                        <tr>
                            <th width="300">Balance</th>
                            <td></td>
                        </tr>
                        <tr>
                            <th>Texs</th>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            </div>
        </div><br><br>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Txn Hash</th>
                                <th>Block</th>
                                <th>Age</th>
                                <th>From</th>
                                <th> </th>
                                <th>To</th>
                                <th>Value</th>
                                <th>[Txn Free]</th>

                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in tx">
                              <td><router-link :to="{name: 'explorer.tx.detail', params: { hash: item.hash }}" >{{ item.hash | truncate(10) }}</router-link></td>
                              <td>{{ item.blockNumber}}</td>
                              <td>{{ item.저장일시}}</td>
                              <td><router-link :to="{ name: 'explorer.address.detail', params: { address: item.from }}">{{ item.from | truncate(10) }}</router-link></td>
                              <td></td>
                              <td><router-link :to="{ name: 'explorer.address.detail', params: { address: item.to }}">{{ item.to | truncate(10) }}</router-link></td>
                              <td></td>
                              <td>{{ item.gasPrice}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `
    ,
    data() {
        return {
            address: "",
            isValid: true,
            balance: "",
            txCount: "",
            tx: []
        }
    },
    methods: {
        fetchTxes: function () {
            var scope = this;

            
            console.log("parms데이터", this.address)
            etheriumService.findTranByAddress(this.address, function (response) {
                console.log("check", response.trans.length)
                for (let i = 0; i < response.trans.length; i++) {
                    etheriumService.findBlockById(response.trans[i].blockNumber, function (blcokdata) {
                        response.trans[i].저장일시 = etheriumService.timeSince(blcokdata.timestamp);
                    })
                }
                console.log("데이터가져오나...", response)
                console.log("데이터가져오나..2.", response.trans)
                scope.tx = response.trans
            })

        }
    },
    mounted: function () {
        this.address = this.$route.params.address;
        this.fetchTxes();
        this.$nextTick(function () {
            window.setInterval(() => {
                this.fetchTxes();
            }, REFRESH_TIMES_OF_TRANSACTIONS);
        })
    }
})

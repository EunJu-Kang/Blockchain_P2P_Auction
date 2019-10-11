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
                            <th width="100">Balance</th>
                            <td>{{balance | truncate(5)}}</td>
                        </tr>
                        <tr>
                            <th>Tx Count</th>
                            <td>{{txCount}}</td>
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
                                <th>Status</th>
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
                              <td>
                              <span v-if= "address === item.from ">{{ item.from | truncate(10) }} </span>
                              <span v-else> <router-link :to="{ name: 'explorer.address.detail', params: { address: item.from }}">{{ item.from | truncate(10) }}</router-link>  </span></td>
                              <td>
                              <span class="badge badge-primary" v-if="address === item.from ">OUT </span>
                              <span class="badge badge-danger" v-else>IN </span>
                              </td>
                              <td>
                              <span v-if= "address === item.to "> {{ item.to | truncate(10) }}</span>
                              <span v-else> <router-link :to="{ name: 'explorer.address.detail', params: { address: item.to }}">{{ item.to | truncate(10) }}</router-link> </span></td>
                              </td>
                              <td>{{item.gasPrice}} Ether</td>
                              <td>{{ item.gas}}</td>
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
            etheriumService.findTranByAddress(this.address, function (response) {

                for (let i = 0; i < response.trans.length; i++) {
                    etheriumService.findBlockById(response.trans[i].blockNumber, function (blcokdata) {
                        response.trans[i].저장일시 = etheriumService.timeSince(blcokdata.timestamp);
                    })

                    web3.eth.getTransaction(response.trans[i].hash).then(tranhash => {
                      var EtherValue = (tranhash.value/Math.pow(10, 18)).toFixed(3);

                      if(EtherValue.substring([EtherValue.length-3], [EtherValue.length]) === "000"){
                        response.trans[i].gasPrice = (tranhash.value/Math.pow(10, 18));
                      }else{
                        response.trans[i].gasPrice = EtherValue;
                      }
                    response.trans[i].gas = (tranhash.gasPrice)
                    });
                }
                scope.tx = response.trans
                scope.balance = response.balance;
                scope.txCount = response.txCount;
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

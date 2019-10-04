var exploreraddressDetailView = Vue.component('ExploreraddressDetailView', {
    template:  `
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
                            <tr v-for="(item, index) in contracts">
                                <td><router-link :to="{ name: 'explorer.auction.detail', params: { contractAddress: item } }">{{ item | truncate(15) }}</router-link></td>
                                <td>
                                    <span class="badge badge-primary" v-if="items[index] && !items[index].ended">OUT</span>
                                    <span class="badge badge-danger" v-if="items[index] && items[index].ended">IN</span>
                                </td>
                                <td>{{ items[index] && items[index].higestBid }} ETH</td>
                                <td>
                                    // <span v-if="items[index] && items[index].higestBid != 0">{{ items[index] && items[index].higestBidder | truncate(15) }}</span>
                                    // <span v-if="items[index] && items[index].higestBid == 0">-</span>
                                </td>
                                <td>{{ items[index] && items[index].endTime.toLocaleString() }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `
,
data(){
    return {
        address: "",
        isValid: true,
        balance: "",
        txCount: "",
        tx: {
          txhash: "",
          blockId: "",
          timestamp: "",
          from: "",
          to: "",
          amount: "",
          accepted: "",
          ststus: "",
          gas: "",
          gasPrice: ""
        }
    }
},
mounted: function(){
  this.address=this.$route.params.address;
  console.log("address", this.address)
  var scope = this;
        if(this.address) {
          console.log("mounted 확인");
          etheriumService.findTranByAddress(this.address,function(response){
            console.log("mounted 확인2");
            scope.tx = respons
          })
        } else {
        this.isValid = false;
    }
}
})

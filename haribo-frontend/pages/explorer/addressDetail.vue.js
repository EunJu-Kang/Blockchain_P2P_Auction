var exploreraddressDetailView = Vue.component('ExploreraddressDetailView', {
    template:  `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Address Explorer" description="검색한 주소의 결과를 보여줍니다."></v-breadcrumb>
        <div class="container">
        <br><br>
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
                <div class="card shadow-sm">
                    <div class="card-header"><strong> Transactions </strong></div>
                    <table class="table">
                        <tbody>
                            <tr>
                                <th width="300">트랜잭션 해시</th>
                                <td>{{tx.txHash}}</td>
                            </tr>
                            <tr>
                                <th>블록 넘버</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>날짜</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>송신자 주소</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>전송한 이더</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>Gas</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>Gas Price</th>
                                <td> bytes</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
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

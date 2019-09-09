var explorerBlockDetailView = Vue.component('ExplorerBlockDetailView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Block Explorer" description="블록체인에서 생성된 블록내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                <div class="card shadow-sm">
                    <div class="card-header">블록 <strong># {{ block.blockNo }}</strong></div>
                    <table class="table">
                        <tbody>
                            <tr>
                                <th width="300">블록 height</th>
                                <td>{{ block.blockNo }}</td>
                            </tr>
                            <tr>
                                <th>블록 해시</th>
                                <td>{{ block.hash }}</td>
                            </tr>
                            <tr>
                                <th>블록 생성 시간</th>
                                <td>{{ block.timestamp }}</td>
                            </tr>
                            <tr>
                                <th>Miner</th>
                                <td><a href="#">{{ block.miner }}</a></td>
                            </tr>
                            <tr>
                                <th>Nonce</th>
                                <td>{{ block.nonce }}</td>
                            </tr>
                            <tr>
                                <th>문제 난이도 (Difficulty)</th>
                                <td>{{ block.difficulty }}</td>
                            </tr>
                            <tr>
                                <th>블록 크기</th>
                                <td>{{ block.size }} bytes</td>
                            </tr>
                            <tr>
                                <th>gasLimit</th>
                                <td>{{ block.gasLimit }}</td>
                            </tr>
                            <tr>
                                <th>gasUsed</th>
                                <td>{{ block.gasUsed }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                </div>
            </div>
        </div>
    </div>
    `,
    data() {
         return {
             isValid: true,
             block: {
               number: "",
               hash: "",
               timestamp: "",
               miner: "",
               nonce: "",
               difficulty: "",
               size: "",
               gasLimit: "",
               gasUsed: ""
             }
         }
     },
     mounted: function(){
         // TODO
         var scope = this;
         var blockNumber=this.$route.params.blockNumber;
         if(blockNumber) {
             etheriumService.findBlockById(blockNumber,function(response){
               response.timestamp = Date(response.timestamp).toString()
               scope.block = response
             })
         }  else {
             this.isValid = false;
         }
     },
 })

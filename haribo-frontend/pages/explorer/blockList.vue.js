var explorerBlockView = Vue.component("ExplorerBlockView", {
  template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Block Explorer" description="블록체인에서 생성된 블록내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <div id="blocks" class="col-md-8 mx-auto">
                        <div class="card shadow-sm">
                            <div class="card-header">Blocks</div>
                            <div class="card-body">
                                <div class="row block-info" v-for="item in blocks">
                                    <div class="col-md-2">BK</div>
                                    <div class="col-md-4">
                                        <router-link :to="{name:'explorer.block.detail', params: {blockNumber:item.blockNo}}" class="block-number">{{ item.blockNo }}</router-link>
                                        <p class="block-timestamp">{{ item.timestamp }}</p>
                                    </div>
                                    <div class="col-md-6 text-right">
                                        <p class="block-num-transactions">{{ item.txCount }} Txes</p>
                                    </div>
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
      lastReadBlock: 0,
      blocks: []
    };
  },
  methods: {
    fetchBlocks: function(){
        var scope = this;
        etheriumService.recentBlock(function (response){
          for(let i=0; i<10; i++){
            let a = Math.round(new Date(response[i].timestamp).getTime())+32400000;
            a = new Date(a)
            response[i].timestamp = etheriumService.timeSince(a)
            if(response[i].trans){
              response[i].txCount = response[i].trans.length
            } else {
              response[i].txCount = 0
            }
          }
          scope.blocks = response
        })
   }
  },
  mounted: function() {
    this.fetchBlocks();

    this.$nextTick(function() {
      window.setInterval(() => {
        this.fetchBlocks();
      }, REFRESH_TIMES_OF_BLOCKS);
    });
  }
});

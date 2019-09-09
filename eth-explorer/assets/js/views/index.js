const NUMBER_OF_CONTENTS_TO_SHOW = 3; // 한 번에 보여줄 정보의 개수
const REFRESH_TIMES_OF_OVERVIEW = 1000; // 개요 정보 갱신 시간 1초
const REFRESH_TIMES_OF_BLOCKS = 5000; // 블록 정보 갱신 시간 5초
const REFRESH_TIMES_OF_TRANSACTIONS = 3000; // 트랜잭션 정보 갱신 시간 3초

// 실제 Vue 템플릿 코드 작성 부분
$(function() {
  var dashboardOverview = new Vue({
    el: "#dashboard-overview",
    data: {
      latestBlock: 0,
      latestTxCount: 0
    },
    methods: {
      updateLatestBlock: async function() {
        fetchLatestBlock().then(r => {
          this.latestBlock = r;
        });
        // console.log(await web3.eth.getBlock(39));
      },
      updateLatestTxCount: async function() {
        this.latestTxCount = (await web3.eth.getBlock(
          this.latestBlock
        )).transactions.length;
      }
    },
    mounted: function() {
      this.$nextTick(function() {
        window.setInterval(() => {
          this.updateLatestBlock();
          this.updateLatestTxCount();
        }, REFRESH_TIMES_OF_OVERVIEW);
      });
    }
  });

  var blocksView = new Vue({
    el: "#blocks",
    data: {
      lastReadBlock: 0,
      blocks: []
    },
    methods: {
      fetchBlocks: async function() {
        // TODO 최근 3개의 블록 정보를 가져와서 계속 업데이트 한다.
        await fetchLatestBlock().then(r => {
          this.latestBlock = r;
        });
        var arr = [];
        for (
          var i = this.latestBlock;
          i > this.latestBlock - NUMBER_OF_CONTENTS_TO_SHOW;
          i--
        ) {
          arr.push({
            number: i,
            timestamp: timeSince((await web3.eth.getBlock(i)).timestamp),
            txCount: (await web3.eth.getBlock(i)).transactions.length
          });
        }

        this.blocks = arr;
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

  var txesView = new Vue({
    el: "#transactions",
    data: {
      transactions: []
    },
    methods: {
      fetchTxes: async function() {
        // TODO 최근 블록에 속한 10개의 트랜잭션 정보를 가져와서 계속 업데이트 한다.
        await fetchLatestBlock().then(r => {
          this.latestBlock = r;
        });
        var arr = [];
        var txCnt =
          (await web3.eth.getBlock(this.latestBlock)).transactions.length - 1;
        for (var i = txCnt; i > txCnt - NUMBER_OF_CONTENTS_TO_SHOW; i--) {
          arr.push({
            hash: (await web3.eth.getBlock(this.latestBlock)).transactions[i],
            timeSince: timeSince(
              (await web3.eth.getBlock(this.latestBlock)).timestamp
            ),
            from: (await web3.eth.getTransaction(
              (await web3.eth.getBlock(this.latestBlock)).transactions[i]
            )).from,
            to: (await web3.eth.getTransaction(
              (await web3.eth.getBlock(this.latestBlock)).transactions[i]
            )).to
          });
        }
        this.transactions = arr;
      }
    },
    mounted: function() {
      this.fetchTxes();

      this.$nextTick(function() {
        window.setInterval(() => {
          this.fetchTxes();
        }, REFRESH_TIMES_OF_TRANSACTIONS);
      });
    }
  });
});

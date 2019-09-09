const NUMBER_OF_CONTENTS_TO_SHOW = 10; // 한 번에 보여줄 정보의 개수
const REFRESH_TIMES_OF_TRANSACTIONS = 3000; // 트랜잭션 정보 갱신 시간 3초

// 실제 Vue 템플릿 코드 작성 부분
$(function() {
  var txesView = new Vue({
    el: "#transactions",
    data: {
      transactions: []
    },
    methods: {
      fetchTxes: async function() {
        // TODO
        var latestBlock;
        await fetchLatestBlock().then(r => {
          latestBlock = r;
        });
        var arr = [];
        var txCnt =
          (await web3.eth.getBlock(latestBlock)).transactions.length - 1;
        for (
          var i = txCnt;
          i >= 0 && i > txCnt - NUMBER_OF_CONTENTS_TO_SHOW;
          i--
        ) {
          arr.push({
            hash: (await web3.eth.getBlock(latestBlock)).transactions[i],
            timeSince: timeSince(
              (await web3.eth.getBlock(latestBlock)).timestamp
            ),
            from: (await web3.eth.getTransaction(
              (await web3.eth.getBlock(latestBlock)).transactions[i]
            )).from,
            to: (await web3.eth.getTransaction(
              (await web3.eth.getBlock(latestBlock)).transactions[i]
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

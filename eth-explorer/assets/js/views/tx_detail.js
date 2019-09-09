// 실제 Vue 템플릿 코드 작성 부분
$(function() {
  var hash = parseQueryString()["hash"];

  var detailView = new Vue({
    el: "#tx-detail",
    data: {
      isValid: true,
      tx: {
        hash: "-"
      },
      timestamp: "-"
    },
    mounted: async function() {
      if (hash) {
        // TODO
        this.tx = await web3.eth.getTransaction(hash);
        this.timestamp = new Date(
          (await web3.eth.getBlock(this.tx.blockNumber)).timestamp * 1000
        );
        console.log(this.tx);
      } else {
        this.isValid = false;
      }
    }
  });
});

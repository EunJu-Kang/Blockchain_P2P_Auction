// 실제 Vue 템플릿 코드 작성 부분
$(function() {
  var blockNumber = parseQueryString()["blockNumber"];

  var detailView = new Vue({
    el: "#block-detail",
    data: {
      isValid: true,
      block: {}
    },
    mounted: async function() {
      if (blockNumber) {
        // TODO
        this.block = await web3.eth.getBlock(blockNumber);
        console.log(this.block)
      } else {
        this.isValid = false;
      }
    }
  });
});

var store = {
    debug: true,
    state: {
        isSigned: false,
        user: {
            id: "",                // 사용자 아이디 저장
            hasWallet: false      // 지갑을 가지고 있는지 여부 조회
        }
    },
    setMessageAction (newValue) {
      if (this.debug) console.log('setMessageAction triggered with', newValue)
      this.state.message = newValue
    },
    clearMessageAction () {
      if (this.debug) console.log('clearMessageAction triggered')
      this.state.message = ''
    }
}

  var filter = function(text, length, clamp){
    clamp = clamp || '...';
    var node = document.createElement('div');
    node.innerHTML = text;
    var content = node.textContent;
    return content.length > length ? content.slice(0, length) + clamp : content;
  };

Vue.filter('truncate', filter);

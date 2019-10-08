var navVue = Vue.component("v-nav", {
    props: ["isSigned"],
    template: `
        <nav class="navbar navbar-expand-lg fixed-top navbar-dark nav-height nav-style">
            <div class="container">
                <router-link class="navbar-brand nav-fontColor" to="/">Chain Villain</router-link>
                <div class="navbar-collapse offcanvas-collapse" id="navbarsExampleDefault">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item">
                            <router-link class="nav-link nav-fontColor" to="/artworks">Artworks</router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link nav-fontColor" to="/auction">Auction</router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link nav-fontColor" to="/explorer/auctions">Explorer</router-link>
                        </li>
                        <li class="nav-item" v-if="sharedState.isSigned">
                            <router-link class="nav-link nav-fontColor" to="/mypage/wallet_create" v-if="!sharedState.user.hasWallet">Mypage</router-link>
                            <router-link class="nav-link nav-fontColor" to="/mypage/wallet_info" v-if="sharedState.user.hasWallet">Mypage</router-link>
                        </li>
                        <li class="nav-item" v-if="!sharedState.isSigned">
                            <router-link class="nav-link nav-fontColor" to="/login">Login</router-link>
                        </li>
                        <li class="nav-item" v-if="!sharedState.isSigned">
                            <router-link class="nav-link nav-fontColor" to="/register">SignUp</router-link>
                        </li>
                        <li class="nav-item" v-if="sharedState.isSigned">
                            <router-link class="nav-link nav-fontColor" to="/login" @click.native="logout">Logout</router-link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    `,
    data() {
        return {
            sharedState: store.state
        }
    },
    methods: {
      loginCheck: function() {
        var scope = this
        var userid = sessionStorage.getItem("userID")
        if(userid != null){
          userService.findById(
              userid,
              function(){
                  store.state.isSigned = true;
                  store.state.user.id = userid;
                  walletService.findById(userid, function(response){
                      if(response) {
                        scope.sharedState.user.hasWallet = true;
                      } else{
                        scope.sharedState.user.hasWallet = false;
                      }
                  });
              }
          );
        }
      },
      logout : function(){
          store.state.isSigned = false
          store.state.user.id = ""
          store.state.hasWallet = false
          sessionStorage.removeItem("userID")
          sessionStorage.removeItem("sign")
        }
    },
    mounted: function(){
      this.loginCheck()
    }
})

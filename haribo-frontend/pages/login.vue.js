var loginView = Vue.component('LoginView', {
    template: `
        <div class="container bg-style">
            <div class="row">
                <div id="login-form" class="col-md-6 mx-auto form-style">
                    <router-link class="link-style" to="/">Auction | HARIBO</router-link>
                    <div class="mt-4">
                        <div class="form-group">
                            <label for="email">이메일</label>
                            <input type="text" class="form-control" id="email" v-model="user.email" placeholder="이메일">
                        </div>
                        <div class="form-group">
                            <label for="password">비밀번호</label>
                            <input type="password" @keyup.enter="login" class="form-control" id="pw" v-model="user.password" placeholder="비밀번호">
                        </div>
                        <button type="submit" class="btn form-style btn-style" style="background-color:#2A3247; color:white;" v-on:click="login">로그인</button>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            user: {
                email: '',
                password: '',
            }
        }
    },
    methods: {
        login: function() {
            var scope = this;
            var shaPW = CryptoJS.SHA256($('#pw').val()).toString();
            userService.login(
                this.user.email,
                shaPW,
                function(data){
                    store.state.isSigned = true;
                    store.state.user.id = data.id;
                    sessionStorage.setItem("userID", data.id)
                    sessionStorage.setItem("sign", true)
                    walletService.findById(store.state.user.id, function(response){
                        if(response) {
                          store.state.user.hasWallet = true;
                        } else{
                          store.state.user.hasWallet = false;
                        }
                    });

                    scope.$router.push('/');
                },
                function(error){
                    alert("유저 이메일 혹은 비밀번호가 일치하지 않습니다.");
                }
            );
        }
    }
})

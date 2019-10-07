var registerView = Vue.component('RegisterView', {
    template: `
        <div class="container bg-style">
            <div class="row">
                <div id="register-form" class="col-md-6 mx-auto form-style">
                    <router-link class="link-style" to="/">Auction | HARIBO</router-link>
                    <div class="mt-4">
                        <div class="form-group">
                            <label for="email">이메일</label>
                            <input type="text" class="form-control" id="email" v-model="user.email" placeholder="이메일">
                        </div>
                        <div class="form-group">
                            <label for="name">이름</label>
                            <input type="text" class="form-control" id="name" v-model="user.name" placeholder="이름">
                        </div>
                        <div class="form-group">
                            <label for="password">비밀번호</label>
                            <input type="password" class="form-control" id="pw" v-model="user.password" placeholder="비밀번호">
                        </div>
                        <div class="form-group">
                            <label for="password-confirm">비밀번호 확인</label>
                            <input type="password" class="form-control" id="pwConfirm" v-model="user.passwordConfirm" placeholder="비밀번호 확인">
                        </div>
                        <button type="submit" class="btn form-style" style="background-color:#2A3247; color:white;" v-on:click="register">회원가입</button>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            user: {
                email: '',
                name: '',
                password: '',
                passwordConfirm: ''
            }
        }
    },
    methods: {
        register: function () {
            var scope = this;
            var reg_email= this.user.email
            if(!(this.CheckEmail(reg_email))){
                alert("이메일 형식이 잘못되었습니다");
                return;
            }
            var shaPW = CryptoJS.SHA256($('#pw').val()).toString();
            var shaPWC = CryptoJS.SHA256($('#pwConfirm').val()).toString();
            if (this.user.password === this.user.passwordConfirm) {
                userService.signUp(
                    this.user.email,
                    this.user.name,
                    shaPW,
                    function (response) {
                        alert("회원가입이 완료되었습니다.");
                        scope.$router.push('/');
                    }
                );
            } else {
                alert('비밀번호가 일치하지 않습니다.');
            }
        },
        CheckEmail: function (str) {
            var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
            if (!reg_email.test(str)) {
                return false;
            }
            else {
                return true;
            }
        }
    }
})

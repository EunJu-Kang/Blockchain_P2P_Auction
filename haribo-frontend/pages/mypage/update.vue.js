var myUpdateView = Vue.component('MyUpdateView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="개인정보수정" description="개인정보를 수정할 수 있습니다."></v-breadcrumb>
            <div class="container">
                <v-mypage-nav></v-mypage-nav>
                <div class="row">
                    <div class="col-md-6 mx-auto mt-5">
                        <div class="card">
                            <div class="card-header">개인정보수정</div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label id="name">이름</label>
                                    <input id="name" type="text" class="form-control" placeholder="이름" v-model="input.name">
                                </div>
                                <div class="form-group">
                                    <label id="password">비밀번호 확인</label>
                                    <input id="pw" type="password" class="form-control" placeholder="비밀번호 확인" v-model="input.password">
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <button class="btn btn-sm btn-warning" v-on:click="update" >개인정보 수정</button>
                                    </div>
                                    <div class="col-md-6 text-right">
                                        <button class="btn btn-sm btn-outline-secondary" v-on:click="goBack">이전으로 돌아가기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            user: {
                id: 0,
                email: "",
                name: "",
                password: ""
            },
            input: {
                name: "",
                password: ""
            },
            sharedStates: store.state
        }
    },
    methods: {
        update: function(){
            var shaPW = CryptoJS.SHA256($('#pw').val()).toString();
            if(this.user.password !== shaPW){
                alert("입력하신 비밀번호가 일치하지 않습니다.");
                return;
            }
            if(this.input.name === "") {
                alert("이름을 입력해주세요.");
                return;
            }
            userService.update({
                "이메일": this.user.email,
                "이름": this.input.name,
                "비밀번호": shaPW
            }, function(data){
                alert("이름이 변경되었습니다.");
                router.push('/');
            });
        },
        goBack: function(){
            this.$router.go(-1);
        }
    },
    mounted: function(){
        var scope = this;

        userService.findById(this.sharedStates.user.id, function(data){
            scope.user.id = data["id"];
            scope.user.email = data["이메일"];
            scope.user.name = data["이름"];
            scope.user.password = data["비밀번호"];
        });
    }
})

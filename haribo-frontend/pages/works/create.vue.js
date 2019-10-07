var worksCreateView = Vue.component("worksCreateView", {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="작품 등록" description="새로운 작품을 등록합니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-8 mx-auto">
                        <div class="card">
                            <div class="card-body">
                                <div class="form-group">
                                    <label id="name">작품 이름</label>
                                    <input type="text" class="form-control" id="name" v-model="work.name">
                                </div>
                                <div class="form-group">
                                    <label id="description">작품 설명</label>
                                    <textarea class="form-control" id="description" v-model="work.description"></textarea>
                                </div>
                                <div class="form-group">
                                    <label id="imginsert">작품 선택</label>
                                    <input v-on:change='onFileChange' type='file' ref='artimage' class="form-control" id="imgInsert"></input>
                                </div>
                                <div class="form-group">
                                    <label id="isActive">공개여부(공개하려면 체크)</label><br>
                                    <input type="checkbox" id="isActive" v-model="work.isActive">
                                </div>
                                <img :src="outimage">
                                <button type="button" class="btn btn-primary" v-on:click="save">작품 등록하기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            work: {
                name: "",
                description: "",
                isActive: true,
                status: true,
                image:""
            },
            sharedStates: store.state,
            outimage:""
        }
    },
    methods: {
        save: function(){
            var scope = this;
            console.log(scope.work.image)

            workService.create({
                "이름": this.work.name,
                "설명": this.work.description,
                "공개여부": this.work.isActive ? "Y" : "N",
                "상태": this.work.status ? "Y" : "N",
                "회원id": this.sharedStates.user.id,
                "작품이미지":this.work.image
            },
            function(){
                alert('작품이 등록되었습니다.');
                scope.$router.push('/artworks');
            },
            function(error){
                alert("입력폼을 모두 입력해주세요.");
            });
        },
        // fileSelect() {
        //   console.log(this.$refs);
        //   var file = this.$refs.artimage.files[0];
        //   console.log(file)
        //   console.log(file["name"])
        //   var reader = new FileReader(file);
        //   reader.onload = function(){
        //     this.work.image = reader.result;
        //     console.log(this.work.image)
        //   }
        //   // this.work.image = this.$refs.artimage.files[0];
        // },
        onFileChange(e) {
          var files = e.target.files || e.dataTransfer.files;
          if (!files.length)
          return;
          this.createImage(files[0]);
        },
        createImage(file) {
          var image = new Image();
          var reader = new FileReader();
          var vm = this;

          reader.onload = (e) => {
            vm.outimage = e.target.result;
            this.work.image= reader.result;
          };
          reader.readAsDataURL(file);
        }
    }
});

/**
 * 파일명 : routes.js
 * 설명
 *   Vue는 주로 싱글 페이지 어플리케이션(SPA)를 개발할 때 이용합니다. 
 *   원래 보통의 웹페이지는 URL이 변경되면 전체 내용을 다시 서버로부터 받아와서 내용을 업데이트 합니다.
 *   SPA는 전체 내용을 업데이트 하지않고 변경된 부분만을 업데이트 하기 때문에 일반 웹페이지보다 반응이 빠릅니다.
 *   이 파일은 그 SPA를 구현하기 위한 파일로서 매칭되는 URL에 어떤 화면을 보여줄지 결정하는 설정 정보가 포함됩니다.
 * 
 *   해당 페이지로 이동할 때는 브라우저 입력창에 직접 `path`를 입력하셔도 되고
 *   자바스크립트로 this.$router.push('/') 해당 코드를 사용해서 이동할수도 있고
 *   <router-link to="/">메인으로 이동 </router-link>과 같이 태그를 사용할 수도 있습니다.
 */
const routes = [{
    name: "home",
    path: '/',
    component: homeView
},{
    name: "artworks",
    path: '/artworks',
    component: artworksView
},{
    name: "auction",
    path: '/auction',
    component: auctionView
},{
    name: "login",
    path: '/login',
    component: loginView
},{
    name: "register",
    path: '/register',
    component: registerView
},{
    name: "mypage.wallet.create",
    path: '/mypage/wallet_create',
    component: walletCreateView
},{
    name: "mypage.wallet.info",
    path: '/mypage/wallet_info',
    component: walletInfoView
},{
    name: "mypage.artwork",
    path: '/mypage/artworks',
    component: myArtworkView
},{
    name: "mypage.update",
    path: "/mypage/update",
    component: myUpdateView
},{
    name: "mypage.change_password",
    path: "/mypage/change_password",
    component: myChangePasswordView
},{
    name: "work.create",
    path: '/works/create',
    component: worksCreateView
},{
    name: "work.detail",
    path: "/works/detail/:id",
    component: worksDetailView
},{
    name: "work.update",
    path: "/works/update/:id",
    component: worksUpdateView
},{
    name: "work.by_user",
    path: "/works/user/:id",
    component: worksByUserView
},{
    name: "auction.regsiter",
    path: "/auction/register",
    component: auctionRegisterView
},{
    name: "auction.detail",
    path: '/auction/detail/:id',
    component: auctionDetailView
},{
    name: "auction.bid",
    path: '/auction/bid/:id',
    component: auctionBidView
},{
    name: "explorer.auction",
    path: '/explorer/auctions',
    component: explorerAuctionView
},{
    name: "explorer.auction.detail",
    path: '/explorer/auction/detail/:contractAddress',
    component: explorerAuctionDetailView
},{
    name: "explorer.block",
    path: '/explorer/blocks',
    component: explorerBlockView
},{
    name: "explorer.block.detail",
    path: '/explorer/block/:blockNumber',
    component: explorerBlockDetailView
},{
    name: "explorer.tx",
    path: '/explorer/txes',
    component: explorerTxListView
},{
    name: "explorer.tx.detail",
    path: '/explorer/tx/:hash',
    component: explorerTxDetailView
}];
Vue.component('v-breadcrumb', {
    props: ["title", "description"],
    template: `
        <div class="breadcrumb">
            <div class="container">
                <h4>{{ title }}</h4>
                <p>{{ description }}</p>
            </div>
        </div>
    `    
})
<template>
  <div>
    <hr />
    <h2 class="title">Get Started</h2>
    <div v-if="features && features.length" class="features">
      <a v-for="(feature, index) in features"
        :key="index"
        class="feature"
        :href="feature.link">
        <h2>{{ feature.title }}</h2>
        <p>{{ feature.details }}</p>
        <div class="badges">
          <i v-if="feature.ios" class="fab fa-apple ios"></i> 
          <i v-if="feature.android" class="fab fa-android android"></i> 
          <i v-if="feature.jvm" class="fab fa-java jvm"></i></div>
      </a>
    </div>
  </div>
</template>

<script>
module.exports = {
  data: function () {
    return {
      features: [
        {title: 'GraphQL', android: true, ios: true, jvm: true, details: 'Type safe GraphQL client', link: '/graphql/'},
        {title: 'FS', android: true, ios: true, jvm: true, details: 'File System I/O', link: '/fs/'},
        {title: 'Cache', android: true, ios: true, details: 'Thread safe, persistent cache', link: '/cache/'},
        {title: 'Concurrency', android: true, ios: true, details: 'Multithreading made easy', link: '/concurrency/'}, 
      ]
    }
  }
}
</script>

<style scoped lang="stylus">
.title 
  border none 
  margin-top 36px !important
  margin-bottom 24px
.features 
  border none
  padding 1.2rem 0
  margin-top 0
  display flex
  flex-wrap wrap
  align-items flex-start
  align-content stretch
  justify-content space-between
  display: grid;
  grid-row-gap: 24px;
  grid-column-gap: 24px;
  grid-template-rows: auto;
  justify-content: center;
  grid-template-columns: calc(33% - 8px) calc(33% - 8px) calc(33% - 8px);
  .feature
    flex-grow 1
    flex-basis 30%
    max-width 100%
    box-shadow 0 3px 8px 0 rgba(116, 129, 141, 0.1)
    padding 16px
    border 1px solid $borderColor
    border-radius 2px
    cursor pointer
    transition all 0.2s ease-in

    &:hover 
      border-color: $accentColor
      h2
        color: $accentColor

    h2
      margin 0
      padding 0
      font-size 16px
      font-weight 500
      border-bottom none
      padding-bottom 0
      color lighten($textColor, 10%)
      transition all 0.2s ease-in
    p
      margin 0
      margin-top 8px
      padding 0
      color lighten($textColor, 40%)
      font-weight 400

  .badges
    display flex
    justify-content flex-end

    i 
      margin-left 6px
  .ios
    color #FF3852

  .jvm
    color #007FFF
  
  .android 
    color #6CC551
</style>
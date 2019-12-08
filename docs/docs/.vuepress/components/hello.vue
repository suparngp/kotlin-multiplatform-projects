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
      </a>
    </div>
  </div>
</template>

<script>
module.exports = {
  data: function () {
    return {
      features: [
        {title: 'GraphQL', details: 'Type safe GraphQL client', link: '/graphql/'},
        {title: 'FS', details: 'File System I/O', link: '/fs/'},
        {title: 'Cache', details: 'Thread safe, persistent cache', link: '/cache/'},
        {title: 'Concurrency', details: 'Multithreading made easy', link: '/concurrency/'}, 
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
</style>
<template>
<div>
  <h2 class="title">Projects</h2>
    <a
    v-if="features && features.length"
    class="features"
  >
    <div
      v-for="(feature, index) in features"
      :key="index"
      class="feature"
    >
      <h2>{{ feature.title }}</h2>
      <p>{{ feature.details }}</p>
    </div>
  </a>
</div>
</template>

<script>
module.exports = {
  data: function () {
    return {
      features: [
        {title: 'GraphQL', details: 'Details'},
        {title: 'FS', details: 'Details'},
        {title: 'Cache', details: 'Details'},
        {title: 'Concurrency', details: 'Details'}, 
      ]
    }
  }
}
</script>

<style scoped lang="stylus">
.title 
  border none 
  border-top 1px solid $borderColor
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
      margin 0
      padding 0
      font-size 16px
      font-weight 500
      border-bottom none
      padding-bottom 0
      color lighten($textColor, 10%)
      &:hover 
        color: $accentColor
    p
      margin 0
      margin-top 8px
      padding 0
      color lighten($textColor, 25%)
</style>
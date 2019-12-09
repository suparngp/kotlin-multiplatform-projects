module.exports = {
  title: '',
  description: 'Suparnatural Projects',
  themeConfig: {
    logo: '/assets/logo.png',
    sidebar: {
      '/graphql/': ['/graphql/', '/graphql/concepts/'],
      '/fs/': ['/fs/'],
      '/cache/': ['/cache/'],
      '/concurrency/': ['/concurrency/']
    },
    sidebarDepth: 3
  },
  markdown: {
    lineNumbers: true
  },
  head: [
    ['link', { rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css' }]
  ]
}
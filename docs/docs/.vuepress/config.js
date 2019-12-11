module.exports = {
  title: 'Suparnatural Kotlin Multiplatform',
  description: 'Suparnatural Projects for Kotlin Multiplatform apps',
  themeConfig: {
    logo: '/assets/logo.png',
    sidebar: {
      '/graphql/': [
        {
          title: 'suparnatural-graphql',
          collapsable: false,
          children: []
        },
        {
          title: 'Guide',
          collapsable: false,
          children: [
            '',
            'installation',
          ]
        },
        {
          title: 'Concepts',
          collapsable: false,
          children: [
            'concepts'
          ]
        }
      ],
      '/fs/': ['/fs/'],
      '/cache/': ['/cache/'],
      '/concurrency/': ['/concurrency/']
    },
    displayAllHeaders: true,
    sidebarDepth: 3
  },
  markdown: {
    lineNumbers: true
  },
  head: [
    ['link', { rel: 'stylesheet', href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css' }]
  ]
}
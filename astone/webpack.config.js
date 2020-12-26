module.exports = {
  entry: {
    "astone-fastopt": ['/home/piquerez/dashpi2/astone/astone/target/scala-3.0.0-M3/scalajs-bundler/main/astone-fastopt.js']
  },
  output: {
    path: '/home/piquerez/dashpi2/astone/astone/target/scala-3.0.0-M2/scalajs-bundler/main',
    filename: '[name]-bundle.js'
  },
  mode: 'development',
  devtool: 'source-map',
  module: {
    rules: [
      {
        test: /\.js$/i,
        enforce: 'pre',
        use: ['source-map-loader']
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        use: ['url-loader']
      }
    ]
  }
}
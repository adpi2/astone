module.exports = {
  entry: {
    "astone-fastopt": ['/home/piquerez/dashpi2/astone/astone/target/scala-3.0.0-M3/scalajs-bundler/main/astone-fastopt.js']
  },
  output: {
    path: '/home/piquerez/dashpi2/astone/astone/target/scala-3.0.0-M3/scalajs-bundler/main',
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
        test: /\.(png|svg|jpe?g|gif|cascade)$/i,
        use: ['url-loader']
      },
      // {
      //   test: /\.(cascade)$/i,
      //   use: ['raw-loader']
      // }
      // {
      //   test: /\.(wasm)$/i,
      //   use: ['file-loader'],
      //   type: 'javascript/auto'
      // }
    ]
  }
}
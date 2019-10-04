const webpack = require('webpack');
var path = require('path');

module.exports = {
    entry: './src/main/frontend/initialize.js',
    devtool: 'sourcemaps',
    cache: false,
    mode: 'development',
    output: {
        path: __dirname,
        //for mwnw run
        filename: './src/main/resources/static/built/bundle.js'
        //for debug pupouse
        // filename: './target/classes/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            }
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            "React": "react",
            "ReactDOM": "react-dom"
        }),
    ],
};
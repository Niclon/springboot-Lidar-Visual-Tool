const webpack = require('webpack');
var path = require('path');

module.exports = {
    // entry: './src/main/frontend/initialize.js',
    entry: {
        selectionScripts: './src/main/frontend/initialize.js',
        replayScripts: './src/main/frontend/initializeLoad.js'
    },
    devtool: 'sourcemaps',
    cache: false,
    mode: 'development',
    output: {
        path: __dirname,
        //for mwnw run
        // filename: './src/main/resources/static/built/bundle.js'
        //for debug pupouse
        filename: './target/classes/static/built/[id].js'
    },
};
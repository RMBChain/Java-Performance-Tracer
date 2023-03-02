const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    const backendUrl = process.env.REACT_APP_BACKEND_URL;
    app.use(
        '/api',
        createProxyMiddleware({
            target: backendUrl ? backendUrl : 'http://localhost:8899',
            changeOrigin: true,
        })
    );
};

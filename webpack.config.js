module.exports = (env, argv) => {
	const dev = argv.mode === "development";
	return {
		entry: "./src/main/typescript/Application.tsx",
		resolve: {
			extensions: [".js", ".jsx", ".ts", ".tsx"]
		},
		module: {
			rules: [{ test: /\.[tj]sx?$/, loader: "ts-loader" }]
		},
		output: {
			path: require("path").resolve("./src/main/resources/public/"),
			filename: "bundle-[hash].js"
		},
		plugins: [new (require("html-webpack-plugin"))({})],
		devtool: dev && "eval-source-map",
		devServer: {
			port: 3000,
			proxy: {
				"/api": "http://localhost:8080"
			}
		}
	};
};

import { h, render, Component } from "preact";

class Application extends Component<{}, { count: number }> {
	constructor(props = {}) {
		super(props);
		this.state = { count: 0 };
	}

	increment(this: Application) {
		this.setState({ count: this.state.count + 1 });
	}

	render() {
		return (
			<div>
				<button onClick={() => this.increment()}>Increment</button>
				<br />
				Counter: {this.state.count}
			</div>
		)
	}
}

render(<Application />, document.body.appendChild(document.createElement("span")))

import { h, Component } from "preact";

export default class LineButtonInput extends Component<
	{ defaultValue?: string; submit: (this: void, value: string) => void },
	{ value: string }
> {
	componentWillMount() {
		this.setState({ value: this.props.defaultValue || "" });
	}

	handleChange(this: LineButtonInput, event: Event) {
		this.setState({ value: event.currentTarget ? (event.currentTarget as HTMLInputElement).value : "" });
	}

	render() {
		return (
			<div>
				<input type="text" value={this.state.value} onChange={this.handleChange.bind(this)} />
				<button onClick={() => this.props.submit(this.state.value)}>{this.props.children}</button>
			</div>
		);
	}
}

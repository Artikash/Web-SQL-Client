import { h, render, Component } from "preact";
import LineButtonInput from "./LineButtonInput";

class Application extends Component<{}, { queryResults: Array<any> }> {
	state = { queryResults: [] };

	connectionId = "";

	async connect(this: Application, fullDatabaseUrl: string) {
		const response = await fetch("/api/connect", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ fullDatabaseUrl })
		});
		if (!response.ok) return;
		this.connectionId = await response.text();
	}

	async sendQuery(this: Application, query: string) {
		const response = await fetch("/api/query", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ connectionId: this.connectionId, query })
		});
		if (!response.ok) return;
		const responseData = await response.json();
		if (!(responseData instanceof Array)) return;
		this.setState({ queryResults: responseData });
	}

	render() {
		const columns = Object.keys(this.state.queryResults[0] || {});
		return (
			<div>
				<LineButtonInput submit={this.connect.bind(this)}>Connect</LineButtonInput>
				<LineButtonInput submit={this.sendQuery.bind(this)}>Send query</LineButtonInput>
				<div>
					Sample DB: mysql://rfamro@mysql-rfam-public.ebi.ac.uk:4497/Rfam
					<br />
					Sample query: SELECT description, comment, author FROM family
				</div>
				<table>
					<tr>
						{columns.map(column => (
							<th>{column}</th>
						))}
					</tr>
					{this.state.queryResults.map(row => (
						<tr>
							{columns.map(column => (
								<td>{row[column]}</td>
							))}
						</tr>
					))}
				</table>
			</div>
		);
	}
}

render(<Application />, document.body.appendChild(document.createElement("span")));

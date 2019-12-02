import { h, render } from "preact";
import LineButtonInput from "../../main/typescript/LineButtonInput";

describe("Components", () => {
	let scratch: HTMLSpanElement;

	beforeAll(() => {
		(document.body || document.documentElement).appendChild((scratch = document.createElement("span")));
	});

	beforeEach(() => {
		scratch.innerHTML = "";
	});

	afterAll(() => {
		scratch.parentNode?.removeChild(scratch);
	});

	describe("Line Button Input", () => {
		it("should render a button and input", () => {
			render(<LineButtonInput submit={(_: string) => 0}>With some text inside</LineButtonInput>, scratch);
			expect(scratch.innerHTML).toContain("button");
			expect(scratch.innerHTML).toContain("input");
		});
	});
});

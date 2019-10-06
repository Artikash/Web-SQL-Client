import Adder from "../../main/typescript/Adder";

test("Correct addition", () => {
	const adder = Adder(3, 4);
	expect(adder()).toBeCloseTo(7);
});

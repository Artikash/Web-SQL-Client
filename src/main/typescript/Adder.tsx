export default (a: number, b: number) => {
	const sum = a + b;
	return () => sum;
}

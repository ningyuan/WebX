/**
 * 
 */

let height = 200;

let width = 800;

margin = ({top: 20, right: 30, bottom: 30, left: 30});

let numX = 100;

let x = d3.scaleLinear()
	.domain([0, numX-1])
	.range([margin.left, width - margin.right]);

let y = d3.scaleLinear()
	.domain([0, 4])
    .range([height - margin.bottom, margin.top]);

let line = d3.line()
	.x(d => x(d.step))
	.y(d => y(d.value));

let value = 2;

function calculateNextValue(r) {
	value += r - 0.5;
	value = Math.max(Math.min(value, 4), 0);
};
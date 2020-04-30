/**
 * 
 */

let drag = simulation => {
	  function dragstarted(d) {
		  if (!d3.event.active) 
			  simulation.alphaTarget(0.3).restart();
		  d.fx = d.x;
		  d.fy = d.y;
	  }
	  
	  function dragged(d) {
		  d.fx = d3.event.x;
		  d.fy = d3.event.y;
	  }
	  
	  function dragended(d) {
		  if (!d3.event.active) 
			  simulation.alphaTarget(0);
		  d.fx = null;
		  d.fy = null;
	  }
	  
	  return d3.drag()
	      .on("start", dragstarted)
	      .on("drag", dragged)
	      .on("end", dragended);
};

function paintGraph (data) {
	let links = [];
	let nodes = [];
  		
  	const svg = d3.select("body")
  				  .select("#chart-container")
      			  .attr("viewBox", [-width / 2, -height / 2, width, height]);
  	
  	// remove old lines
  	svg.select("#lines")
       .selectAll("line")
       .data(links)
       .exit()
       .remove();
  				
    links = data.links.map(d => Object.create(d));	
    	
    // remove old circles
    svg.select("#circles")
	   .selectAll("circle")
	   .data(nodes)
	   .exit()
	   .remove();
    	
    nodes = data.nodes.map(d => Object.create(d));	
    	
    const simulation = d3.forceSimulation(nodes)
			 .force("link", d3.forceLink(links).id(d => d.id))
			 .force("charge", d3.forceManyBody())
			 .force("x", d3.forceX())
			 .force("y", d3.forceY());
    
    // add new lines
    let link =	svg.select("#lines")
    				.attr("stroke", "#999")
    				.attr("stroke-opacity", 0.6)
    				.selectAll("line")
    				.data(links)
    				.join("line")
      				.attr("stroke-width", Math.sqrt(4));
    
    // add new circles
 	let node = svg.select("#circles")
      				.attr("stroke", "#fff")
      				.attr("stroke-width", 1.5)
    				.selectAll("circle")
    				.data(nodes)
    				.join("circle")
      				.attr("r", d => d.radius)
      				.attr("fill", d => d.color)
      				.call(drag(simulation));

  	node.append("title")
      	.text(d => d.name);

  	simulation.on("tick", () => {
    	link
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y);

    	node
        .attr("cx", d => d.x)
        .attr("cy", d => d.y);
  	});
};

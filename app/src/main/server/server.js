module.exports = (context, cb)=>{
	const angle = (context.query.angle * Math.PI)/180;
	const velocity = context.query.velocity;
	
	const vX = Math.sin(angle) * velocity;
	const vY = Math.cos(angle) * velocity;
	
	let result = [{t: 0.0, x: 0.0, y: 0.0}];
	
	let t = 0;
    let y = 0;
    let x = 0;
    for (; y >= 0; t += .1) {
        y = vX * t - 9.81 * t * t / 2;
        x = vY * t;
        result.push({t, x, y});
    }

    cb(null, result);

};
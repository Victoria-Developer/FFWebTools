let canvasPanel;
const canvasScaledPointsMap = new Map();

$(document).ready(function() {
    canvasPanel = document.getElementById("canvasPanel");
});

function getLogInfoText(log) {
    return `${log.name} (${log.x}, ${log.y})`;
}

function updateMapCanvas(response) {
    const canvasPanel = document.getElementById("canvasPanel");
    canvasPanel.innerHTML = "";
    const width = 800;
    const height = 800;

    response.forEach((data) => {
        const canvas = document.createElement("canvas");
        canvasPanel.appendChild(canvas);
        const ctx = canvas.getContext("2d");

        const background = new Image();
        background.src = "/images/areas" + data.areaFileName;

        background.onload = function () {
            const logs = data.logs;

            canvas.width = width;
            canvas.height = height;
            ctx.drawImage(background, 0, 0, width, height);

            for (let step = 0; step < logs.length; step++) {
                ctx.strokeStyle = "purple";
                ctx.font = "bold 16px serif";
                ctx.fillStyle = "#ff0000";
                ctx.lineWidth = 3;
                ctx.beginPath();

                const log = logs[step];
                let logFileName = log.teleport == true? "tp.png" : "x_mark.png"
                const src = "/images/" + logFileName;

                // Calculate scaled game point
                const scaledPoint1 = scalePoint(log, height, width);
                canvasScaledPointsMap.set(log, { x: scaledPoint1.x, y: scaledPoint1.y });
                ctx.moveTo(scaledPoint1.x, scaledPoint1.y);
                var matches = log.name.match(/\b(\w)/g);
                var acronym = matches.join('')  + `(${log.x}, ${log.y})`;
                addMarker(src, ctx, scaledPoint1.x, scaledPoint1.y, acronym);

                // Draw line between two points on the map
                if (step !== logs.length - 1 && logs[step + 1].teleport === false) {
                    const scaledPoint2 = scalePoint(logs[step + 1], height, width);
                    ctx.lineTo(scaledPoint2.x, scaledPoint2.y);
                }
                ctx.stroke();
            }

            // Add tooltip listener for the drawn map
            canvas.addEventListener("mousemove", (e) => {
                drawToolTip(e, canvas, logs);
            });
        };
    });
}

// Scale point to the map image
function scalePoint(point, imageHeight, imageWidth) {
    const differenceInStartPoint = 1;
    const gameX = point.x - differenceInStartPoint;
    const gameY = point.y - differenceInStartPoint;

    const cellNumber = 41;
    const cellHeight = imageHeight / cellNumber;
    const cellWidth = imageWidth / cellNumber;

    const resultY = cellHeight * gameY;
    const resultX = cellWidth * gameX;

    return {
        x: resultX,
        y: resultY
    };
}

function addOutLineText(ctx, text, x, y) {
    const fontSize = 16;
    const fontFace = "verdana";
    ctx.font = `${fontSize}px ${fontFace}`;
    const textWidth = ctx.measureText(text).width;

    ctx.textAlign = "center";
    ctx.textBaseline = "middle";
    ctx.strokeStyle = "black";
    ctx.strokeText(text, x, y);
    ctx.fillStyle = "white";
    ctx.fillText(text, x, y);
}

function addMarker(src, ctx, x, y, text) {
    const marker = new Image();
    marker.src = src;

/*
    ctx.strokeStyle = "black";
    ctx.fillStyle = "red";
    ctx.beginPath();
    ctx.arc(x, y, textWidth / text.toString().length, 0, 2 * Math.PI, false);
    ctx.stroke();
    ctx.fill();
*/
    marker.onload = function() {
        const imgWidth = 30;
        const imgHeight = 30;
        const imgWidthCenter = imgWidth / 2;
        const imgHeightCenter = imgHeight / 2;
        const imageX = x - imgWidthCenter;
        const imageY = y - imgHeightCenter;

        ctx.drawImage(marker, imageX, imageY, imgWidth, imgHeight);
        addOutLineText(ctx, text, imageX + imgWidthCenter, imageY);
    };
}

function drawToolTip(e, canvas, logs) {
    e.preventDefault();
    e.stopPropagation();
    canvas.title = "";

    const bounds = canvas.getBoundingClientRect();
    const currentX = e.clientX - bounds.left;
    const currentY = e.clientY - bounds.top;
    const margin = 7;

    for (let index in logs) {
        const log = logs[index];
        const point = canvasScaledPointsMap.get(log);
        const x = point.x;
        const y = point.y;

        if (Math.abs(x - currentX) < margin && Math.abs(y - currentY) < margin) {
            canvas.title = getLogInfoText(log);
        }
    }
}
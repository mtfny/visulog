const modules = document.getElementsByClassName("module");
let modulesArray = [];
let moduleNumber = 0;

//console.log("------------------");
for (let i = 0; i<modules.length; i++) {
    //console.log(modules[i].innerHTML);
    modulesArray.push(modules[i]);
}
console.log(modulesArray.length + " modules have been detected, work in progress...");
/*console.log("------------------");
console.log('.');

console.log("moduleLength : " + modulesArray.length);*/
for (let i = 0; i<modulesArray.length; i++) {
    /*console.log("------------------");
    console.log("moduleName : " + modulesArray[i].innerHTML);
    console.log(modulesArray[i]);
    console.log(modulesArray[i].parentElement);*/
    if(modulesArray[i].innerHTML === "commitActivity") {
        commitActivity(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }

    if(modulesArray[i].innerHTML === "fileCounter") {
        fileCounter(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }

    if(modulesArray[i].innerHTML === "contributorActivity") {
        contributorActivity(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }

    if(modulesArray[i].innerHTML === "languages") {
        langageUsage(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }
}



function commitActivity(module) {
    let moduleInfo = {
        'name': "commit Activity",
        'desc': "Graphique représentant le nombre de commit effectué par jour"
    }
    let moduleNext = module.nextElementSibling;
    let divs = placeTemplate(moduleInfo, [module, moduleNext]);
    if (divs != null) {
        divs.projectStatDisplay.appendChild(module);
    }else {
        document.getElementsByClassName("project-stat-display")[0].appendChild(module);
    }
    let grid = document.getElementsByClassName("days")[0];
    let bubble = document.getElementById("bubble");

    let child = grid.children;

    let gridContainer = document.getElementById('stats-grid');
    let dayListContainer = document.getElementById('day-list');
    let takedWidth = gridContainer.clientWidth + dayListContainer.clientWidth;
    let freeWidth = moduleNext.clientWidth;
    console.log(takedWidth, freeWidth);
    if (takedWidth >= freeWidth) {
        let parent = gridContainer.parentElement.parentElement
        parent.style.borderBottomLeftRadius = "0px";
        parent.style.borderBottomRightRadius = "0px";
        parent.addEventListener('wheel', (event) => {
            parent.scrollLeft += (event.deltaY * 1.2);
            event.preventDefault();
        })
    }

    for (let i = 0; i<child.length; i++) {
        let multi = child[i].getAttribute("data-commit-number");
        child[i].style.background = "rgb("+ multi * 20 + "," + multi * 50 + "," + multi * 20+ ")";
    }

    for (let i = 0; i<child.length; i++) {
        console.log(window.pageYOffset)
        child[i].onmouseover = (element) => {
            bubble.removeAttribute("hidden");
            bubble.style.top = window.scrollY + element.target.getBoundingClientRect().y - 40 + "px";
            bubble.style.left = element.target.getBoundingClientRect().x -15 + "px";
            bubble.style.opacity=1;

            bubble.innerHTML = element.target.getAttribute("data-date") + " : " + element.target.getAttribute("data-commit-number") + " commits";
        }
        child[i].onmouseleave = () => {
            bubble.setAttribute("hidden", "");
            bubble.style.opacity=0;
        }
    }
    console.log(moduleInfo.name + " is ready !");
}

function contributorActivity(module) {
    let moduleInfo = {
        'name': "Contributor Activity",
        'desc': "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués."
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let data = document.getElementById("data-contributor-activity");
    let dataObject = convertToObject(data);
    let canvasParent;
    if (divs == null) {
        let description = document.getElementsByClassName("project-stat-desc")[moduleNumber].firstElementChild
        description.innerHTML = "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués.";
        canvasParent = document.getElementsByClassName("project-stat-display")[0]
    }else {
        canvasParent = divs.projectStatDisplay;
    }
    let canvasDiv = document.createElement("canvas");
    canvasParent.appendChild(canvasDiv);

    let myChart = new Chart(canvasDiv, {
        type: 'bar',
        data: {
            labels: dataObject.contributors,
            datasets: [{
                label: '# of Commit',
                data: dataObject.commitNumbers,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)',
                    'rgba(255, 54, 79, 0.2)',
                    'rgba(32, 54, 179, 0.2)',
                    'rgba(250, 54, 179, 0.2)',
                    'rgba(17, 67, 201, 0.2)',
                    'rgba(135, 80, 104, 0.2)',
                    'rgba(18, 164, 177, 0.2)',
                    'rgba(28, 184, 102, 0.2)',
                    'rgba(252, 118, 23, 0.2)',
                    'rgba(175, 185, 129, 0.2)',
                    'rgba(159, 217, 214, 0.2)',
                    'rgba(233, 35, 159, 0.2)',
                    'rgba(69, 207, 74, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(255, 54, 79, 1)',
                    'rgba(32, 54, 179, 1)',
                    'rgba(250, 54, 179, 1)',
                    'rgba(17, 67, 201, 1)',
                    'rgba(135, 80, 104, 1)',
                    'rgba(18, 164, 177, 1)',
                    'rgba(28, 184, 102, 1)',
                    'rgba(252, 118, 23, 1)',
                    'rgba(175, 185, 129, 1)',
                    'rgba(159, 217, 214, 1)',
                    'rgba(233, 35, 159, 1)',
                    'rgba(69, 207, 74, 1)'
                ],
                borderWidth: 1
            }],
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    display: true,
                    ticks: {
                        beginAtZero: true,
                        min: 0
                    }
                }],
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    })
    console.log(moduleInfo.name + " is ready !");
}

/* FILE COUNTER */
function fileCounter(module) {
    let moduleInfo = {
        'name': "File Counter",
        'desc': "Compte le nombre de fichier"
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let parent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[0].firstElementChild
        description.innerHTML = moduleInfo.desc;
        let title = document.getElementsByClassName("project-desc-title")[0]
        description.innerHTML = moduleInfo.name;
        parent = canvasParent = document.getElementsByClassName("project-stat-display")[0]
    }else {
        parent = divs.projectStatDisplay;
    }
    parent.style.display = "flex";
    parent.style.justifyContent = "center";
    parent.style.alignItems = "center";
    let divNumber = document.createElement('div');
    divNumber.id = "file-counter-container";
    divNumber.innerHTML = "0 files";
    divNumber.style.fontSize = "5em";
    parent.appendChild(divNumber);
    let fileNumberContainer = document.getElementById('data-file-number');
    let fileNumber = fileNumberContainer.firstElementChild.getAttribute('data-file-number');
    console.log(moduleInfo.name + " is ready !");
    animateValue(divNumber, 0, fileNumber, 1000);
}
/* ************ */



/* LANGAGE DISTRIBUTION */
function langageUsage(module) {
    let moduleInfo = {
        'name': "langage Usage",
        'desc': "Répartition de l'usage des langage"
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let parent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[0].firstElementChild
        description.innerHTML = moduleInfo.desc;
        let title = document.getElementsByClassName("project-desc-title")[0]
        description.innerHTML = moduleInfo.name;
        parent = canvasParent = document.getElementsByClassName("project-stat-display")[0]
    }else {
        parent = divs.projectStatDisplay;
    }

    let dataDiv = document.getElementById("data-langages-parts");
    let dataObject = getLanguageData(dataDiv);

    let canvasLangages = document.createElement("canvas");
    parent.appendChild(canvasLangages);

    let myChart = new Chart(canvasLangages, {
        type: 'doughnut',
        data: {
            labels: dataObject.langages,
            datasets: [{
                label: '# percentage',
                data: dataObject.percentages,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 1
            }],
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    display: true,
                    ticks: {
                        beginAtZero: true,
                        min: 0
                    }
                }],
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    })
    console.log(moduleInfo.name + " is ready !");
}
/* ******************** */

/* OBJECT CONVERTISSER */
function convertToObject(document) {
    let child = document.children;
    let data = {
        'contributors' : [],
        'commitNumbers': []
    }
    for (let i = 0; i<child.length; i++) {
        data.contributors.push(child[i].getAttribute("data-contributor-name"));
        data.commitNumbers.push(parseInt(child[i].innerHTML));
    }
    return data;
}

function getLanguageData(document) {
    let child = document.children;
    let data = {
        'langages' : [],
        'percentages': []
    }
    for (let i = 0; i<child.length; i++) {
        data.langages.push(child[i].getAttribute("data-langages-parts"));
        data.percentages.push(parseInt(child[i].innerHTML));
    }
    return data;
}
/* ******************* */


/* UTILS */
function animateValue(element, start, end, duration) {
    if (start === end) return;
    var range = end - start;
    var current = start;
    var increment = end > start? 1 : -1;
    var stepTime = Math.abs(Math.floor(duration / range));
    var timer = setInterval(function() {
        current += increment;
        element.innerHTML = current + " files";
        if (current == end) {
            clearInterval(timer);
        }
    }, stepTime);
}
/* ***** */


/* HTML GENERATOR */
function createProjectInfoDiv(moduleName, moduleDesc) {
    let projectInfo = document.createElement('div');
    projectInfo.classList.add('project-info');

    let projectDescContainer = document.createElement('div');
    let projectStatContainer = document.createElement('div');
    projectDescContainer.classList.add('project-desc-container');
    projectStatContainer.classList.add('project-stat-container');


    let projectStatDesc = document.createElement('div');
    let projectStat = document.createElement('div');
    let projectStatDisplay = document.createElement('div');
    projectStatDesc.classList.add('project-stat-desc');
    projectStat.classList.add('project-stat');
    projectStatDisplay.classList.add('project-stat-display');

    projectInfo.appendChild(projectDescContainer);
    projectInfo.appendChild(projectStatContainer);

    let title = document.createElement('h2');
    title.innerHTML = moduleName;
    projectStatContainer.appendChild(title);
    projectStatContainer.appendChild(projectStatDesc);
    projectStatContainer.appendChild(projectStat);

    let desc = document.createElement('p');
    desc.innerHTML = moduleDesc;
    projectStatDesc.appendChild(desc);

    projectStat.appendChild(projectStatDisplay);
    let returnObject = {
        'projectInfo': projectInfo,
        'projectStat': projectStat,
        'projectStatDisplay': projectStatDisplay
    }

    return returnObject; 
}

function placeTemplate(moduleInfo, element) {
    if(moduleNumber === 0) {return null;}
    //console.log("m"+moduleNumber);
    let container = document.getElementById('container');
    //console.log("c"+container)
    let divs = createProjectInfoDiv(moduleInfo.name, moduleInfo.desc);
    //console.log("d"+divs.projectInfo);
    //console.log("d"+divs.projectStat);
    //console.log("d"+divs.projectStatDisplay);
    for (let i = 0; i<element.length; i++) {
        //console.log(element[i])
        divs.projectStatDisplay.appendChild(element[i]);
    }
    container.appendChild(divs.projectInfo);
    return divs;
}
/* ************** */



/* IMAGE SWITCHER */
let clickImgNumber = 0;
let currentLinkNumber = 1;
let imgLink = [
    "https://www.fun-academy.fr/wp-content/uploads/2021/02/Genshin-Impact-Bennett-birthday-official-artwork-mihoyo-ps4-ps5-pc-switch-mobile-.jpg",
    "./display/kleeProject.jpg"
];
let img = document.getElementById('image');
img.addEventListener('click', event => {
    clickImgNumber++;
    console.log(clickImgNumber);
    if (clickImgNumber === 2) {
        clickImgNumber = 0;
        if (currentLinkNumber === 1) {
            img.setAttribute('src', imgLink[0]);
        }else {
            img.setAttribute('src', imgLink[1]);
        }
        currentLinkNumber*=-1;
    }
})
/* ************** */
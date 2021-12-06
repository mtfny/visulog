const modules = document.getElementsByClassName("module");
let modulesArray = [];
let moduleNumber = 0;

let moduleContainer = document.getElementById("module-container");
let menuLink = document.getElementById("menu-link"); 

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
        let fileInfo = {
            'title': "File Counter",
            'name' : "file-counter-anchor",
            'desc' : "Compte le nombre de fichier"
        }
        moduleCounter(modulesArray[i], fileInfo, 'file');
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

    if(modulesArray[i].innerHTML === "mergeCounter") {
        let mergeInfo = {
            'title': "Merge Number",
            'name' : "merge-counter-anchor",
            'desc' : "Compte le nombre de merges effectué."
        }
        moduleCounter(modulesArray[i], mergeInfo, 'merge');
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }

    if(modulesArray[i].innerHTML === "linesPerContributor") {
        linesPerContributor(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }

    if(modulesArray[i].innerHTML === "linesPerDay") {
        linesPerDay(modulesArray[i]);
        moduleNumber++;
        //console.log("l"+moduleNumber);
    }
}



function commitActivity(module) {
    let moduleInfo = {
        'title': "commit Activity",
        'name' : "commit-activity-anchor",
        'desc' : "Graphique représentant le nombre de commit effectué par jour"
    }
    let moduleNext = module.nextElementSibling;
    let divs = placeTemplate(moduleInfo, [module, moduleNext]);
    if (divs != null) {
        divs.projectStatDisplay.appendChild(module);
        moduleContainer.appendChild(divs.projectInfo);
    }else {
        document.getElementsByClassName("project-stat-display")[0].appendChild(module);
    }
    let grid = document.getElementsByClassName("days")[0];
    let bubble = document.getElementById("bubble");

    let startDay = parseInt(grid.getAttribute("data-day-start"));
    grid.firstElementChild.style.gridRowStart = startDay;


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
        'title': "Contributor Activity",
        'name' : "contributor-activity-anchor",
        'desc': "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués."
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let data = document.getElementById("data-contributor-activity");
    let dataObject = convertToObject(data);
    let canvasParent;
    if (divs == null) {
        let description = document.getElementsByClassName("project-stat-desc")[moduleNumber].firstElementChild
        description.innerHTML = "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués.";
        canvasParent = document.getElementsByClassName("project-stat-display")[0];
    }else {
        moduleContainer.appendChild(divs.projectInfo);
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
                backgroundColor: generateArrayColor(dataObject.contributors.length, '0.2'),
                borderColor: generateArrayColor(dataObject.contributors.length, '1'),
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


/* MERGE COUNTER */
function moduleCounter(module, moduleInfo, name) {
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let parent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[0].firstElementChild;
        description.innerHTML = moduleInfo.desc;
        let title = document.getElementsByClassName("project-desc-title")[0];
        description.innerHTML = moduleInfo.name;
        parent = canvasParent = document.getElementsByClassName("project-stat-display")[0];
    }else {
        moduleContainer.appendChild(divs.projectInfo);
        parent = divs.projectStatDisplay;
    }
    parent.style.display = "flex";
    parent.style.justifyContent = "center";
    parent.style.alignItems = "center";
    let divNumber = document.createElement('div');
    divNumber.id = name + "-counter-container";
    divNumber.innerHTML = "0 files";
    divNumber.style.fontSize = "5em";
    parent.appendChild(divNumber);
    let fileNumberContainer = document.getElementById('data-' + name + '-number');
    let fileNumber = fileNumberContainer.firstElementChild.getAttribute('data-' + name + '-number');
    console.log(moduleInfo.name + " is ready !");
    animateValue(divNumber, 0, fileNumber, 1000);
}
/* ************ */


/* LINES PER DAY */
function linesPerDay(module) {
    let moduleInfo = {
        'title': "Lines Per Day",
        'name' : "lines-per-day-anchor",
        'desc': "..."
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let parent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[0].firstElementChild;
        description.innerHTML = moduleInfo.desc;
        let title = document.getElementsByClassName("project-desc-title")[0];
        description.innerHTML = moduleInfo.name;
        parent = document.getElementsByClassName("project-stat-display")[0];
    }else {
        moduleContainer.appendChild(divs.projectInfo);
        parent = divs.projectStatDisplay;
    }

    let dataDiv = document.getElementById("data-lines-per-day");
    let dataObject = getLinesPerDayData(dataDiv);

    let canvasDiv = document.createElement("canvas");
    parent.appendChild(canvasDiv);

    let myChart = new Chart(canvasDiv, {
        type: 'line',
        data: {
            labels: dataObject.date,
            datasets: [{
                label: '# of Lines',
                data: dataObject.linesNumber,
                backgroundColor: generateArrayColor(dataObject.date.length, '0.2'),
                borderColor: generateArrayColor(dataObject.date.length, '1'),
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
/* ************ */

/* LANGAGE DISTRIBUTION */
function langageUsage(module) {
    let moduleInfo = {
        'title': "langage Usage",
        'name' : "langage-usage-anchor",
        'desc': "Répartition de l'usage des langage"
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let parent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[0].firstElementChild;
        description.innerHTML = moduleInfo.desc;
        let title = document.getElementsByClassName("project-desc-title")[0];
        description.innerHTML = moduleInfo.name;
        parent = canvasParent = document.getElementsByClassName("project-stat-display")[0];
    }else {
        moduleContainer.appendChild(divs.projectInfo);
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
                backgroundColor: generateArrayColor(dataObject.langages.length, '0.2'),
                borderColor: generateArrayColor(dataObject.langages.length, '1'),
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

function linesPerContributor(module) {
    let moduleInfo = {
        'title': "Number of Lines per Contributor",
        'name' : "lines-per-contributor-anchor",
        'desc': "This calculation module then displays the number of lines written by a contributor on this project."
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextElementSibling]);
    let data = document.getElementById("data-contributor-lines");
    let dataObject = getLinesData(data);
    let canvasParent;
    if (divs === null) {
        let description = document.getElementsByClassName("project-stat-desc")[moduleNumber].firstElementChild;
        description.innerHTML = moduleInfo.desc;
        canvasParent = document.getElementsByClassName("project-stat-display")[0];
    }else {
        moduleContainer.appendChild(divs.projectInfo);
        canvasParent = divs.projectStatDisplay;
    }
    let canvasDiv = document.createElement("canvas");
    canvasParent.appendChild(canvasDiv);

    let myChart = new Chart(canvasDiv, {
        type: 'bar',
        data: {
            labels: dataObject.contributors,
            datasets: [{
                label: '# of Lines',
                data: dataObject.linesNumber,
                backgroundColor: generateArrayColor(dataObject.contributors.length, '0.2'),
                borderColor: generateArrayColor(dataObject.contributors.length, '1'),
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

function getLinesData(document) {
    let child = document.children;
    let data = {
        'contributors' : [],
        'linesNumber': []
    }
    for (let i = 0; i<child.length; i++) {
        data.contributors.push(child[i].getAttribute("data-contributor"));
        data.linesNumber.push(parseInt(child[i].innerHTML));
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

function getLinesPerDayData(document) {
    let child = document.children;
    let data = {
        'date' : [],
        'linesNumber': []
    }
    for (let i = 0; i<child.length; i++) {
        data.date.push(child[i].getAttribute("data-date"));
        data.linesNumber.push(parseInt(child[i].innerHTML));
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

function generateArrayColor(size, opacity) {
    let colors = [`rgba(255, 99, 132, ${opacity})`,
    `rgba(54, 162, 235, ${opacity})`,
    `rgba(255, 206, 86, ${opacity})`,
    `rgba(75, 192, 192, ${opacity})`,
    `rgba(153, 102, 255, ${opacity})`,
    `rgba(255, 159, 64, ${opacity})`,
    `rgba(255, 54, 79, ${opacity})`,
    `rgba(32, 54, 179, ${opacity})`,
    `rgba(250, 54, 179, ${opacity})`,
    `rgba(17, 67, 201, ${opacity})`,
    `rgba(135, 80, 104, ${opacity})`,
    `rgba(18, 164, 177, ${opacity})`,
    `rgba(28, 184, 102, ${opacity})`,
    `rgba(252, 118, 23, ${opacity})`,
    `rgba(175, 185, 129, ${opacity})`,
    `rgba(159, 217, 214, ${opacity})`,
    `rgba(233, 35, 159, ${opacity})`,
    `rgba(69, 207, 74, ${opacity})`]
    let arrayColor = [];
    let iterator = 0
    for (let i = 0; i<size; i++) {
        arrayColor.push(colors[iterator]);
        iterator++;
        if (iterator === colors.length) {iterator = 0;}
    }
    return arrayColor;
}
/* ***** */


/* HTML GENERATOR */
function createProjectInfoDiv(moduleTitle, moduleName, moduleDesc) {
    let projectInfo = document.createElement('div');
    projectInfo.classList.add('project-info');

    let projectStatContainer = document.createElement('div');
    projectStatContainer.classList.add('project-stat-container');


    let projectStatDesc = document.createElement('div');
    let projectStat = document.createElement('div');
    let projectStatDisplay = document.createElement('div');
    projectStatDesc.classList.add('project-stat-desc');
    projectStat.classList.add('project-stat');
    projectStatDisplay.classList.add('project-stat-display');

    projectInfo.appendChild(projectStatContainer);

    let title = document.createElement('h2');
    title.innerHTML = moduleTitle;
    title.id = moduleName;
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
    let anchor = document.createElement("a");
    anchor.classList.add("anchor");
    anchor.setAttribute("href", "#" + moduleInfo.name);
    anchor.innerHTML = moduleInfo.title;
    anchor.onclick = hideSidebar;
    menuLink.appendChild(anchor);

    if(moduleNumber === 0) {return null;}
    //console.log("m"+moduleNumber);
    let container = document.getElementById('container');
    //console.log("c"+container)
    let divs = createProjectInfoDiv(moduleInfo.title, moduleInfo.name, moduleInfo.desc);
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

/* MENU SLIDER */
let menuLogoButton = document.getElementById("menu-logo");
let menuLogoExit = document.getElementById("menu-logo-exit");
let menu = document.getElementById("menu");
let anchor = document.getElementsByClassName("anchor");

menuLogoButton.onclick = () => {
    menu.style.left = 0;
    menu.style.top = window.scrollY + "px";
    document.body.style.overflow = "hidden";
}

menuLogoExit.onclick = hideSidebar;

function hideSidebar() {
    menu.style.left = "-20vw";
    document.body.style.overflow = "";
}
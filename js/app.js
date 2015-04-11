(function(){

    var app = angular.module('sublingoManager', []);


    app.controller('GameController', ['$scope', '$http', '$sce', function ($scope, $http, $sce) {
        this.count = -1;
        this.level = 0;
        this.category = 0;
        this.clips = [];
        this.subtitle = "";
        this.xp = 0;

        this.currentVideoId = "";


        var req = {
            method: 'GET',
            url: 'https://api.parse.com/1/classes/clips',
            headers: {
                'X-Parse-Application-Id': "eBc3RsK73vT1NPDgIwjwTNbzq5A47dnDjzbBMfhO",
                'X-Parse-REST-API-Key': "jUbjITpOPuPFqduvNdAOX8PXW8zypCfrQKm4oaZ8"
            }
        };
        var that = this;
        $http(req)
            .success(function(response) {
                that.clips = response.results;
            })
            .error(function (response) {
                alert("Failed to get a list of clips");
            });

        this.startGame = function (sectionCtrl, category) {
            var cats = this.clips.filter(function(clip){
                return category.name.toLowerCase() === clip.category;
            });
            if(cats.length == 0){
                alert("No videos on the system for this category, please try another category.");
                return;
            }
            this.category = category;
            this.next();
        };

        this.next = function (sectionCtrl) {
            this.count++;
            this.subtitle = "";
            this.xp += 50;
            if(this.count == 10){
                this.count = -1;
                this.xp += 500;
                return;
            }
            this.getVideoUrl();
        };

        this.getVideoUrl = function () {
            var that = this;
            var cats = this.clips.filter(function(clip){
                return that.category.name.toLowerCase() === clip.category ;
            });
            this.currentVideoId = cats[Math.floor(Math.random()*cats.length)].objectId;
            var url = "https://s3-eu-west-1.amazonaws.com/sublingo/" + this.currentVideoId + ".mp4";
            $scope.videoUrl = $sce.trustAsResourceUrl(url);
        };

        this.handleKeyEvent = function (e, sectionCtrl) {
            if(this.count > -1)
            console.log("handleKeyEvent(" + e.keyCode + ")");
            switch (e.keyCode){
                case 13:
                    e.preventDefault();
                    this.submitVideo(sectionCtrl);
                    return false;
                case 37:
                    e.preventDefault();
                    document.getElementById("video").play();
                    document.getElementById("video").currentTime = document.getElementById("video").currentTime - 1;
                    return false;
                case 38:
                    e.preventDefault();
                    document.getElementById("video").play();
                    document.getElementById("video").currentTime = 0;
                    return false;
                case 27:
                    this.count = -1;
                    return false;
            }
        };

        this.getLevelForXp = function(xp){
            var points = 0;
            var output = 0;
            var minlevel = 2; // first level to display
            var maxlevel = 200; // last level to display

            for (lvl = 1; lvl <= maxlevel; lvl++){
                points += Math.floor(lvl + 300 * Math.pow(2, lvl / 7.));
                output = Math.floor(points / 4);
                if(output > xp){
                    return lvl;
                }
            }
        };


        this.getXPForLevel = function(level){
            var points = 0;
            var output = 0;
            var minlevel = 2; // first level to display
            var maxlevel = 200; // last level to display

            for (lvl = 1; lvl <= maxlevel; lvl++){
                points += Math.floor(lvl + 300 * Math.pow(2, lvl / 7.));
                output = Math.floor(points / 4);
                if(lvl === level){
                    return output;
                }
            }
        };

        this.submitVideo = function (sectionCtrl) {
            var that = this;
            var req = {
                method: 'POST',
                url: 'https://api.parse.com/1/classes/subtitles',
                headers: {
                    'X-Parse-Application-Id': "eBc3RsK73vT1NPDgIwjwTNbzq5A47dnDjzbBMfhO",
                    'X-Parse-REST-API-Key': "jUbjITpOPuPFqduvNdAOX8PXW8zypCfrQKm4oaZ8"
                },
                data: {
                    subtitle: that.subtitle,
                    clip: {
                        __type: "Pointer",
                        className: "clips",
                        objectId: that.currentVideoId
                    }
                }
            };
            $http(req)
                .success(function(response) {
                    console.log("success");
                })
                .error(function (response) {

                });
            this.next(sectionCtrl);
        }
    }]);

    app.controller('SectionController', function () {
        this.categories = [
            {
                name: "All",
                size: 5,
                color: "#8badaf",
                icon: "",
                complete: false
            },
            {
                name: "Comedy",
                size: 3,
                color: "#60898B",
                icon: "",
                complete: false
            },
            {
                name: "Misc",
                size: 4,
                color: "#8badaf",
                icon: "",
                complete: false
            },
            {
                name: "Sport",
                size: 3,
                color: "#60898B",
                icon: "",
                newRow: true,
                complete: false
            },
            {
                name: "Factual",
                size: 6,
                color: "#8badaf",
                icon: "",
                complete: false
            },
            {
                name: "Music",
                size: 3,
                color: "#60898B",
                icon: "",
                complete: false
            }
        ];
    });

})();
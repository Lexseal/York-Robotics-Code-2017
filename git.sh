git add .
x=$(<.count)
let x=x+1
git commit -m "update No. $x"
echo $x > .count
git push -u origin master

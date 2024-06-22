<div class="justify-content-center text-center">
    <p class="font-weight-bold mt-5 h4">
        Select position:
    </p>
    <div>
        <a href="/Controller?command=CurrentAndBetterKeepers" ${selectedPosition == "Goalkeeper" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>Goalkeeper</a>
        <a href="/Controller?command=Players&position=Defender" class="btn disabled" ${selectedPosition == "Defender" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>Defender</a>
        <a href="/Controller?command=Players&position=Midfielder" class="btn disabled" ${selectedPosition == "Midfielder" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>Midfielder</a>
        <a href="/Controller?command=Players&position=Forward" class="btn disabled" ${selectedPosition == "Attacker" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>Attacker</a>
    </div>
</div>
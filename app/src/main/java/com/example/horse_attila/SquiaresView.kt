package com.example.horse_attila

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.horse_attila.model.SquareModel
import org.example.GameField
import org.example.State
class SquaresView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
     lateinit var stateList: List<State>
    private var ic_horse: Drawable
    private var ic_king: Drawable
    private var ic_fire: Drawable
    var GameField : GameField
    var StateKing : State
     var StateHorse : State
    val squareSize = 70
    private var numberRows: Int = 8
    private var numberColumns: Int = 8
    private val squares: MutableList<MutableList<SquareModel>> = mutableListOf()
    private var draggingPiece: SquareModel? = null
    private var draggingIcon: Drawable? = null
    private var draggingX: Float = 0f
    private var draggingY: Float = 0f
    var currentIndex = 0
    init {
        ic_horse = VectorDrawableCompat.create(resources, R.drawable.ic_horse_chess, null)!!
        ic_king = VectorDrawableCompat.create(resources, R.drawable.ic_king_chess, null)!!
        ic_fire = VectorDrawableCompat.create(resources, R.drawable.ic_fire, null)!!
        GameField = GameField(8)
        StateKing = State(0,1)
        StateHorse = State(0,0)
        initializeSquares()
    }

    private val paintGreen = Paint().apply {
        color = Color.parseColor("#90ee90")
        style = Paint.Style.FILL
    }
    private val paint = Paint().apply {
        color = Color.parseColor("#F4E5C0")
        style = Paint.Style.FILL
    }
    private val paintBlack = Paint().apply {
        color = Color.parseColor("#C7AB98")
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = squareSize * numberColumns + 85
        val height = squareSize * numberRows + 60
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paintBlack.textSize = 40f


        for (row in 0 until numberRows) {
            val x = numberColumns * squareSize + 40
            val y = row * squareSize + squareSize / 2 + paintBlack.textSize / 2
            canvas.drawText((row + 1).toString(), x.toFloat(), y.toFloat(), paintBlack)
        }

        for (col in 0 until numberColumns) {
            val x = col * squareSize + squareSize / 2 - paintBlack.measureText((col + 1).toString()) / 2
            val y = numberRows * squareSize + 40
            canvas.drawText((col + 1).toString(), x.toFloat(), y.toFloat(), paintBlack)
        }


        for (row in 0 until numberRows) {
            for (col in 0 until numberColumns) {
                val square = squares[row][col]
                val paintToUse = when {
                    // Используем зелёную краску для клетки, которая окрашена
                    square.is_horse_visited -> paintGreen
                    (row + col) % 2 == 0 -> paint
                    else -> paintBlack
                }
                val left = col * squareSize
                val right = left + squareSize
                val top = row * squareSize
                val bottom = top + squareSize

                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paintToUse)


                if (square.is_horse && draggingPiece != square) {
                    drawIcon(canvas, col, row, ic_horse)
                }
                if (square.is_king && draggingPiece != square) {
                    drawIcon(canvas, col, row, ic_king)
                }
                if (square.is_on_fire) {
                    drawIcon(canvas, col, row, ic_fire)
                }
            }
        }


        draggingIcon?.let { icon ->
            icon.setBounds(
                (draggingX - squareSize * 0.4f).toInt(),
                (draggingY - squareSize * 0.4f).toInt(),
                (draggingX + squareSize * 0.4f).toInt(),
                (draggingY + squareSize * 0.4f).toInt()
            )
            icon.draw(canvas)
        }
    }

    private fun drawIcon(canvas: Canvas, col: Int, row: Int, iconDrawable: Drawable) {
        val iconSize = squareSize * 0.8f
        iconDrawable.setBounds(
            (col * squareSize + (squareSize - iconSize) / 2).toInt(),
            (row * squareSize + (squareSize - iconSize) / 2).toInt(),
            (col * squareSize + (squareSize + iconSize) / 2).toInt(),
            (row * squareSize + (squareSize + iconSize) / 2).toInt()
        )
        iconDrawable.draw(canvas)
    }


    fun moveForward() {
        if (currentIndex < stateList.size - 1) {
            val currentState = stateList[currentIndex]
            squares[currentState.x][currentState.y].is_horse = false
            squares[currentState.x][currentState.y].is_horse_visited = true
            currentIndex++
            val nextState = stateList[currentIndex]
            squares[nextState.x][nextState.y].is_horse = true
        }
        invalidate()
    }


     fun moveBackward() {
        if (currentIndex > 0) {
            val currentState = stateList[currentIndex]
            squares[currentState.x][currentState.y].is_horse = false

            currentIndex--
            val previousState = stateList[currentIndex]
            squares[currentState.x][currentState.y].is_horse_visited = false
            squares[previousState.x][previousState.y].is_horse = true

        }
         invalidate()
    }
    fun changeSize(size: Int) {
        GameField = GameField(size)
        numberRows = size
        numberColumns = size
        initializeSquares()
        requestLayout()
        invalidate()
    }


    private fun initializeSquares() {
        squares.clear()
        for (row in 0 until numberRows) {
            val rowList = mutableListOf<SquareModel>()
            for (col in 0 until numberColumns) {
                rowList.add(
                    SquareModel(
                        id = row * numberColumns + col,
                        x_position = col,
                        y_position = row
                    )
                )
            }
            squares.add(rowList)
        }

        squares[0][0].is_horse = true
        squares[0][1].is_king = true
       squares[1][1].is_on_fire = true
        squares[1][0].is_on_fire = true
        GameField.setBurningField(1, 1)
        GameField.setBurningField(1, 0)
        StateKing = State(0,1)
        StateHorse = State(0,0)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val col = (event.x / squareSize).toInt()
                val row = (event.y / squareSize).toInt()

                if (col in 0 until numberColumns && row in 0 until numberRows) {
                    val square = squares[row][col]
                    if (square.is_horse) {
                        draggingPiece = square
                        draggingIcon = ic_horse
                    } else if (square.is_king) {
                        draggingPiece = square
                        draggingIcon = ic_king
                    }
                    if (!square.is_horse && !square.is_king) {
                        if (square.is_on_fire) {
                            square.is_on_fire = false
                            GameField.deleteBurningField(row, col)
                        } else {
                            square.is_on_fire = true
                            GameField.setBurningField(row, col)
                        }
                        invalidate()
                        return true
                    }
                }
                draggingPiece?.let {
                    draggingX = event.x
                    draggingY = event.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                draggingPiece?.let {
                    draggingX = event.x
                    draggingY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                draggingPiece?.let { piece ->
                    val col = (event.x / squareSize).toInt()
                    val row = (event.y / squareSize).toInt()
                    if (col in 0 until numberColumns && row in 0 until numberRows) {
                        val targetSquare = squares[row][col]
                        if (!targetSquare.is_on_fire) {
                            if (draggingIcon == ic_horse && !targetSquare.is_king) {
                                piece.is_horse = false
                                targetSquare.is_horse = true
                                currentIndex =0
                                stateList = listOf()
                                StateHorse = State(row, col)
                            } else if (draggingIcon == ic_king && !targetSquare.is_horse) {
                                piece.is_king = false
                                targetSquare.is_king = true
                                currentIndex =0
                                stateList = listOf()
                                StateKing = State(row, col)
                            }
                        } else {

                            piece.is_horse = draggingIcon == ic_horse
                            piece.is_king = draggingIcon == ic_king
                        }
                    } else {
                        piece.is_horse = draggingIcon == ic_horse
                        piece.is_king = draggingIcon == ic_king
                    }
                    clearGreenFields()
                    invalidate()
                    draggingPiece = null
                    draggingIcon = null
                }
            }
        }
        return true
    }
     fun clearGreenFields() {
        for (row in 0 until numberRows) {
            for (col in 0 until numberColumns) {
                squares[row][col].is_horse_visited = false
            }
        }
         invalidate()
    }
}
